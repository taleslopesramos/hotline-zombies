/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.btree.utils;

import com.badlogic.gdx.ai.btree.BehaviorTree;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.btree.annotation.TaskAttribute;
import com.badlogic.gdx.ai.btree.annotation.TaskConstraint;
import com.badlogic.gdx.ai.btree.branch.DynamicGuardSelector;
import com.badlogic.gdx.ai.btree.branch.Parallel;
import com.badlogic.gdx.ai.btree.branch.RandomSelector;
import com.badlogic.gdx.ai.btree.branch.RandomSequence;
import com.badlogic.gdx.ai.btree.branch.Selector;
import com.badlogic.gdx.ai.btree.branch.Sequence;
import com.badlogic.gdx.ai.btree.decorator.AlwaysFail;
import com.badlogic.gdx.ai.btree.decorator.AlwaysSucceed;
import com.badlogic.gdx.ai.btree.decorator.Include;
import com.badlogic.gdx.ai.btree.decorator.Invert;
import com.badlogic.gdx.ai.btree.decorator.Random;
import com.badlogic.gdx.ai.btree.decorator.Repeat;
import com.badlogic.gdx.ai.btree.decorator.SemaphoreGuard;
import com.badlogic.gdx.ai.btree.decorator.UntilFail;
import com.badlogic.gdx.ai.btree.decorator.UntilSuccess;
import com.badlogic.gdx.ai.btree.leaf.Failure;
import com.badlogic.gdx.ai.btree.leaf.Success;
import com.badlogic.gdx.ai.btree.leaf.Wait;
import com.badlogic.gdx.ai.btree.utils.BehaviorTreeReader;
import com.badlogic.gdx.ai.btree.utils.DistributionAdapters;
import com.badlogic.gdx.ai.utils.random.Distribution;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.reflect.Annotation;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Field;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.Reader;

public class BehaviorTreeParser<E> {
    public static final int DEBUG_NONE = 0;
    public static final int DEBUG_LOW = 1;
    public static final int DEBUG_HIGH = 2;
    public int debugLevel;
    public DistributionAdapters distributionAdapters;
    private DefaultBehaviorTreeReader<E> btReader;

    public BehaviorTreeParser() {
        this(0);
    }

    public BehaviorTreeParser(DistributionAdapters distributionAdapters) {
        this(distributionAdapters, 0);
    }

    public BehaviorTreeParser(int debugLevel) {
        this(new DistributionAdapters(), debugLevel);
    }

    public BehaviorTreeParser(DistributionAdapters distributionAdapters, int debugLevel) {
        this(distributionAdapters, debugLevel, null);
    }

    public BehaviorTreeParser(DistributionAdapters distributionAdapters, int debugLevel, DefaultBehaviorTreeReader<E> reader) {
        this.distributionAdapters = distributionAdapters;
        this.debugLevel = debugLevel;
        this.btReader = reader == null ? new DefaultBehaviorTreeReader() : reader;
        this.btReader.setParser(this);
    }

    public BehaviorTree<E> parse(String string, E object) {
        this.btReader.parse(string);
        return this.createBehaviorTree(this.btReader.root, object);
    }

    public BehaviorTree<E> parse(InputStream input, E object) {
        this.btReader.parse(input);
        return this.createBehaviorTree(this.btReader.root, object);
    }

    public BehaviorTree<E> parse(FileHandle file, E object) {
        this.btReader.parse(file);
        return this.createBehaviorTree(this.btReader.root, object);
    }

    public BehaviorTree<E> parse(Reader reader, E object) {
        this.btReader.parse(reader);
        return this.createBehaviorTree(this.btReader.root, object);
    }

    protected BehaviorTree<E> createBehaviorTree(Task<E> root, E object) {
        if (this.debugLevel > 1) {
            BehaviorTreeParser.printTree(root, 0);
        }
        return new BehaviorTree<E>(root, object);
    }

    protected static <E> void printTree(Task<E> task, int indent) {
        int i;
        for (i = 0; i < indent; ++i) {
            System.out.print(' ');
        }
        if (task.getGuard() != null) {
            System.out.println("Guard");
            BehaviorTreeParser.printTree(task.getGuard(), indent += 2);
            for (i = 0; i < indent; ++i) {
                System.out.print(' ');
            }
        }
        System.out.println(task.getClass().getSimpleName());
        for (i = 0; i < task.getChildCount(); ++i) {
            BehaviorTreeParser.printTree(task.getChild(i), indent + 2);
        }
    }

    public static class DefaultBehaviorTreeReader<E>
    extends BehaviorTreeReader {
        private static final ObjectMap<String, String> DEFAULT_IMPORTS;
        protected BehaviorTreeParser<E> btParser;
        ObjectMap<Class<?>, Metadata> metadataCache = new ObjectMap();
        Task<E> root;
        String subtreeName;
        Statement statement;
        private int indent;
        ObjectMap<String, String> userImports = new ObjectMap();
        ObjectMap<String, Subtree<E>> subtrees = new ObjectMap();
        Subtree<E> currentTree;
        int currentTreeStartIndent;
        int currentDepth;
        int step;
        boolean isSubtreeRef;
        protected StackedTask<E> prevTask;
        protected StackedTask<E> guardChain;
        protected Array<StackedTask<E>> stack = new Array();
        ObjectSet<String> encounteredAttributes = new ObjectSet();
        boolean isGuard;

        public DefaultBehaviorTreeReader() {
            this(false);
        }

        public DefaultBehaviorTreeReader(boolean reportsComments) {
            super(reportsComments);
        }

        public BehaviorTreeParser<E> getParser() {
            return this.btParser;
        }

        public void setParser(BehaviorTreeParser<E> parser) {
            this.btParser = parser;
        }

        @Override
        public void parse(char[] data, int offset, int length) {
            this.debug = this.btParser.debugLevel > 0;
            this.root = null;
            this.clear();
            super.parse(data, offset, length);
            this.popAndCheckMinChildren(0);
            Subtree<E> rootTree = this.subtrees.get("");
            if (rootTree == null) {
                throw new GdxRuntimeException("Missing root tree");
            }
            this.root = rootTree.rootTask;
            if (this.root == null) {
                throw new GdxRuntimeException("The tree must have at least the root task");
            }
            this.clear();
        }

        @Override
        protected void startLine(int indent) {
            if (this.btParser.debugLevel > 1) {
                System.out.println("" + this.lineNumber + ": <" + indent + ">");
            }
            this.indent = indent;
        }

        private Statement checkStatement(String name) {
            if (name.equals(Statement.Import.name)) {
                return Statement.Import;
            }
            if (name.equals(Statement.Subtree.name)) {
                return Statement.Subtree;
            }
            if (name.equals(Statement.Root.name)) {
                return Statement.Root;
            }
            return Statement.TreeTask;
        }

        @Override
        protected void startStatement(String name, boolean isSubtreeReference, boolean isGuard) {
            if (this.btParser.debugLevel > 1) {
                System.out.println((isGuard ? " guard" : " task") + " name '" + name + "'");
            }
            this.isSubtreeRef = isSubtreeReference;
            Statement statement = this.statement = isSubtreeReference ? Statement.TreeTask : this.checkStatement(name);
            if (isGuard && this.statement != Statement.TreeTask) {
                throw new GdxRuntimeException(name + ": only tree's tasks can be guarded");
            }
            this.statement.enter(this, name, isGuard);
        }

        @Override
        protected void attribute(String name, Object value) {
            boolean validAttribute;
            if (this.btParser.debugLevel > 1) {
                System.out.println("" + this.lineNumber + ": attribute '" + name + " : " + value + "'");
            }
            if (!(validAttribute = this.statement.attribute(this, name, value))) {
                if (this.statement == Statement.TreeTask) {
                    throw this.stackedTaskException(this.getCurrentTask(), "unknown attribute '" + name + "'");
                }
                throw new GdxRuntimeException(this.statement.name + ": unknown attribute '" + name + "'");
            }
        }

        private Field getField(Class<?> clazz, String name) {
            try {
                return ClassReflection.getField(clazz, name);
            }
            catch (ReflectionException e) {
                throw new GdxRuntimeException(e);
            }
        }

        private void setField(Field field, Task<E> task, Object value) {
            field.setAccessible(true);
            Object valueObject = this.castValue(field, value);
            try {
                field.set(task, valueObject);
            }
            catch (ReflectionException e) {
                throw new GdxRuntimeException(e);
            }
        }

        /*
         * Unable to fully structure code
         * Enabled aggressive block sorting
         * Lifted jumps to return sites
         */
        private Object castValue(Field field, Object value) {
            type = field.getType();
            ret = null;
            if (!(value instanceof Number)) ** GOTO lbl27
            numberValue = (Number)value;
            if (type != Integer.TYPE && type != Integer.class) ** GOTO lbl8
            ret = numberValue.intValue();
            ** GOTO lbl50
lbl8: // 1 sources:
            if (type != Float.TYPE && type != Float.class) ** GOTO lbl11
            ret = Float.valueOf(numberValue.floatValue());
            ** GOTO lbl50
lbl11: // 1 sources:
            if (type != Long.TYPE && type != Long.class) ** GOTO lbl14
            ret = numberValue.longValue();
            ** GOTO lbl50
lbl14: // 1 sources:
            if (type != Double.TYPE && type != Double.class) ** GOTO lbl17
            ret = numberValue.doubleValue();
            ** GOTO lbl50
lbl17: // 1 sources:
            if (type != Short.TYPE && type != Short.class) ** GOTO lbl20
            ret = numberValue.shortValue();
            ** GOTO lbl50
lbl20: // 1 sources:
            if (type != Byte.TYPE && type != Byte.class) ** GOTO lbl23
            ret = Byte.valueOf(numberValue.byteValue());
            ** GOTO lbl50
lbl23: // 1 sources:
            if (!ClassReflection.isAssignableFrom(Distribution.class, type)) ** GOTO lbl50
            distributionType = type;
            ret = this.btParser.distributionAdapters.toDistribution("constant," + numberValue, distributionType);
            ** GOTO lbl50
lbl27: // 1 sources:
            if (!(value instanceof Boolean)) ** GOTO lbl31
            if (type != Boolean.TYPE && type != Boolean.class) ** GOTO lbl50
            ret = value;
            ** GOTO lbl50
lbl31: // 1 sources:
            if (value instanceof String) {
                stringValue = (String)value;
                if (type == String.class) {
                    ret = value;
                } else if (type == Character.TYPE || type == Character.class) {
                    if (stringValue.length() != 1) {
                        throw new GdxRuntimeException("Invalid character '" + value + "'");
                    }
                    ret = Character.valueOf(stringValue.charAt(0));
                } else if (ClassReflection.isAssignableFrom(Distribution.class, type)) {
                    distributionType = type;
                    ret = this.btParser.distributionAdapters.toDistribution(stringValue, distributionType);
                } else if (ClassReflection.isAssignableFrom(Enum.class, type)) {
                    for (Enum e : (Enum[])type.getEnumConstants()) {
                        if (!e.name().equalsIgnoreCase(stringValue)) continue;
                        ret = e;
                        break;
                    }
                }
            }
lbl50: // 19 sources:
            if (ret != null) return ret;
            this.throwAttributeTypeException(this.getCurrentTask().name, field.getName(), type.getSimpleName());
            return ret;
        }

        private void throwAttributeNameException(String statement, String name, String expectedName) {
            String expected = " no attribute expected";
            if (expectedName != null) {
                expected = "expected '" + expectedName + "' instead";
            }
            throw new GdxRuntimeException(statement + ": attribute '" + name + "' unknown; " + expected);
        }

        private void throwAttributeTypeException(String statement, String name, String expectedType) {
            throw new GdxRuntimeException(statement + ": attribute '" + name + "' must be of type " + expectedType);
        }

        @Override
        protected void endLine() {
        }

        @Override
        protected void endStatement() {
            this.statement.exit(this);
        }

        private void openTask(String name, boolean isGuard) {
            try {
                Task task;
                if (this.isSubtreeRef) {
                    task = this.subtreeRootTaskInstance(name);
                } else {
                    Task tmpTask;
                    String className = this.getImport(name);
                    if (className == null) {
                        className = name;
                    }
                    task = tmpTask = (Task)ClassReflection.newInstance(ClassReflection.forName(className));
                }
                if (!this.currentTree.inited()) {
                    this.initCurrentTree(task, this.indent);
                    this.indent = 0;
                } else if (!isGuard) {
                    StackedTask<E> stackedTask = this.getPrevTask();
                    this.indent -= this.currentTreeStartIndent;
                    if (stackedTask.task == this.currentTree.rootTask) {
                        this.step = this.indent;
                    }
                    if (this.indent > this.currentDepth) {
                        this.stack.add(stackedTask);
                    } else if (this.indent <= this.currentDepth) {
                        int i = (this.currentDepth - this.indent) / this.step;
                        this.popAndCheckMinChildren(this.stack.size - i);
                    }
                    StackedTask<E> stackedParent = this.stack.peek();
                    int maxChildren = stackedParent.metadata.maxChildren;
                    if (stackedParent.task.getChildCount() >= maxChildren) {
                        throw this.stackedTaskException(stackedParent, "max number of children exceeded (" + (stackedParent.task.getChildCount() + 1) + " > " + maxChildren + ")");
                    }
                    stackedParent.task.addChild(task);
                }
                this.updateCurrentTask(this.createStackedTask(name, task), this.indent, isGuard);
            }
            catch (ReflectionException e) {
                throw new GdxRuntimeException("Cannot parse behavior tree!!!", e);
            }
        }

        private StackedTask<E> createStackedTask(String name, Task<E> task) {
            Metadata metadata = this.findMetadata(task.getClass());
            if (metadata == null) {
                throw new GdxRuntimeException(name + ": @TaskConstraint annotation not found in '" + task.getClass().getSimpleName() + "' class hierarchy");
            }
            return new StackedTask<E>(this.lineNumber, name, task, metadata);
        }

        private Metadata findMetadata(Class<?> clazz) {
            Annotation tca;
            Metadata metadata = this.metadataCache.get(clazz);
            if (metadata == null && (tca = ClassReflection.getAnnotation(clazz, TaskConstraint.class)) != null) {
                Field[] fields;
                TaskConstraint taskConstraint = tca.getAnnotation(TaskConstraint.class);
                ObjectMap<String, AttrInfo> taskAttributes = new ObjectMap<String, AttrInfo>();
                for (Field f : fields = ClassReflection.getFields(clazz)) {
                    Annotation a = f.getDeclaredAnnotation(TaskAttribute.class);
                    if (a == null) continue;
                    AttrInfo ai = new AttrInfo(f.getName(), a.getAnnotation(TaskAttribute.class));
                    taskAttributes.put(ai.name, ai);
                }
                metadata = new Metadata(taskConstraint.minChildren(), taskConstraint.maxChildren(), taskAttributes);
                this.metadataCache.put(clazz, metadata);
            }
            return metadata;
        }

        StackedTask<E> getLastStackedTask() {
            return this.stack.peek();
        }

        StackedTask<E> getPrevTask() {
            return this.prevTask;
        }

        StackedTask<E> getCurrentTask() {
            return this.isGuard ? this.guardChain : this.prevTask;
        }

        void updateCurrentTask(StackedTask<E> stackedTask, int indent, boolean isGuard) {
            this.isGuard = isGuard;
            stackedTask.task.setGuard(this.guardChain == null ? null : this.guardChain.task);
            if (isGuard) {
                this.guardChain = stackedTask;
            } else {
                this.prevTask = stackedTask;
                this.guardChain = null;
                this.currentDepth = indent;
            }
        }

        void clear() {
            this.prevTask = null;
            this.guardChain = null;
            this.currentTree = null;
            this.userImports.clear();
            this.subtrees.clear();
            this.stack.clear();
            this.encounteredAttributes.clear();
        }

        void switchToNewTree(String name) {
            this.popAndCheckMinChildren(0);
            this.currentTree = new Subtree(name);
            Subtree<E> oldTree = this.subtrees.put(name, this.currentTree);
            if (oldTree != null) {
                throw new GdxRuntimeException("A subtree named '" + name + "' is already defined");
            }
        }

        void initCurrentTree(Task<E> rootTask, int startIndent) {
            this.currentDepth = -1;
            this.step = 1;
            this.currentTreeStartIndent = startIndent;
            this.currentTree.init(rootTask);
            this.prevTask = null;
        }

        Task<E> subtreeRootTaskInstance(String name) {
            Subtree<E> tree = this.subtrees.get(name);
            if (tree == null) {
                throw new GdxRuntimeException("Undefined subtree with name '" + name + "'");
            }
            return tree.rootTaskInstance();
        }

        void addImport(String alias, String task) {
            String className;
            if (task == null) {
                throw new GdxRuntimeException("import: missing task class name.");
            }
            if (alias == null) {
                Class clazz = null;
                try {
                    clazz = ClassReflection.forName(task);
                }
                catch (ReflectionException e) {
                    throw new GdxRuntimeException("import: class not found '" + task + "'");
                }
                alias = clazz.getSimpleName();
            }
            if ((className = this.getImport(alias)) != null) {
                throw new GdxRuntimeException("import: alias '" + alias + "' previously defined already.");
            }
            this.userImports.put(alias, task);
        }

        String getImport(String as) {
            String className = DEFAULT_IMPORTS.get(as);
            return className != null ? className : this.userImports.get(as);
        }

        private void popAndCheckMinChildren(int upToFloor) {
            if (this.prevTask != null) {
                this.checkMinChildren(this.prevTask);
            }
            while (this.stack.size > upToFloor) {
                StackedTask<E> stackedTask = this.stack.pop();
                this.checkMinChildren(stackedTask);
            }
        }

        private void checkMinChildren(StackedTask<E> stackedTask) {
            int minChildren = stackedTask.metadata.minChildren;
            if (stackedTask.task.getChildCount() < minChildren) {
                throw this.stackedTaskException(stackedTask, "not enough children (" + stackedTask.task.getChildCount() + " < " + minChildren + ")");
            }
        }

        private void checkRequiredAttributes(StackedTask<E> stackedTask) {
            for (Object entry : stackedTask.metadata.attributes) {
                if (!((AttrInfo)entry.value).required || this.encounteredAttributes.contains((String)entry.key)) continue;
                throw this.stackedTaskException(stackedTask, "missing required attribute '" + (String)entry.key + "'");
            }
        }

        private GdxRuntimeException stackedTaskException(StackedTask<E> stackedTask, String message) {
            return new GdxRuntimeException(stackedTask.name + " at line " + stackedTask.lineNumber + ": " + message);
        }

        static {
            Class[] classes;
            DEFAULT_IMPORTS = new ObjectMap();
            for (Class c : classes = new Class[]{AlwaysFail.class, AlwaysSucceed.class, DynamicGuardSelector.class, Failure.class, Include.class, Invert.class, Parallel.class, Random.class, RandomSelector.class, RandomSequence.class, Repeat.class, Selector.class, SemaphoreGuard.class, Sequence.class, Success.class, UntilFail.class, UntilSuccess.class, Wait.class}) {
                String cn;
                String fqcn = c.getName();
                String alias = "" + Character.toLowerCase(cn.charAt(0)) + ((cn = c.getSimpleName()).length() > 1 ? cn.substring(1) : "");
                DEFAULT_IMPORTS.put(alias, fqcn);
            }
        }

        protected static class Subtree<E> {
            String name;
            Task<E> rootTask;
            int referenceCount;

            Subtree() {
                this(null);
            }

            Subtree(String name) {
                this.name = name;
                this.rootTask = null;
                this.referenceCount = 0;
            }

            public void init(Task<E> rootTask) {
                this.rootTask = rootTask;
            }

            public boolean inited() {
                return this.rootTask != null;
            }

            public boolean isRootTree() {
                return this.name == null || "".equals(this.name);
            }

            public Task<E> rootTaskInstance() {
                if (this.referenceCount++ == 0) {
                    return this.rootTask;
                }
                return this.rootTask.cloneTask();
            }
        }

        private static class AttrInfo {
            String name;
            String fieldName;
            boolean required;

            AttrInfo(String fieldName, TaskAttribute annotation) {
                this(annotation.name(), fieldName, annotation.required());
            }

            AttrInfo(String name, String fieldName, boolean required) {
                this.name = name == null || name.length() == 0 ? fieldName : name;
                this.fieldName = fieldName;
                this.required = required;
            }
        }

        private static class Metadata {
            int minChildren;
            int maxChildren;
            ObjectMap<String, AttrInfo> attributes;

            Metadata(int minChildren, int maxChildren, ObjectMap<String, AttrInfo> attributes) {
                this.minChildren = minChildren < 0 ? 0 : minChildren;
                this.maxChildren = maxChildren < 0 ? Integer.MAX_VALUE : maxChildren;
                this.attributes = attributes;
            }
        }

        protected static class StackedTask<E> {
            public int lineNumber;
            public String name;
            public Task<E> task;
            public Metadata metadata;

            StackedTask(int lineNumber, String name, Task<E> task, Metadata metadata) {
                this.lineNumber = lineNumber;
                this.name = name;
                this.task = task;
                this.metadata = metadata;
            }
        }

        static enum Statement {
            Import("import"){

                @Override
                protected <E> void enter(DefaultBehaviorTreeReader<E> reader, String name, boolean isGuard) {
                }

                @Override
                protected <E> boolean attribute(DefaultBehaviorTreeReader<E> reader, String name, Object value) {
                    if (!(value instanceof String)) {
                        reader.throwAttributeTypeException(this.name, name, "String");
                    }
                    reader.addImport(name, (String)value);
                    return true;
                }

                @Override
                protected <E> void exit(DefaultBehaviorTreeReader<E> reader) {
                }
            }
            ,
            Subtree("subtree"){

                @Override
                protected <E> void enter(DefaultBehaviorTreeReader<E> reader, String name, boolean isGuard) {
                }

                @Override
                protected <E> boolean attribute(DefaultBehaviorTreeReader<E> reader, String name, Object value) {
                    if (!name.equals("name")) {
                        reader.throwAttributeNameException(this.name, name, "name");
                    }
                    if (!(value instanceof String)) {
                        reader.throwAttributeTypeException(this.name, name, "String");
                    }
                    if ("".equals(value)) {
                        throw new GdxRuntimeException(this.name + ": the name connot be empty");
                    }
                    if (reader.subtreeName != null) {
                        throw new GdxRuntimeException(this.name + ": the name has been already specified");
                    }
                    reader.subtreeName = (String)value;
                    return true;
                }

                @Override
                protected <E> void exit(DefaultBehaviorTreeReader<E> reader) {
                    if (reader.subtreeName == null) {
                        throw new GdxRuntimeException(this.name + ": the name has not been specified");
                    }
                    reader.switchToNewTree(reader.subtreeName);
                    reader.subtreeName = null;
                }
            }
            ,
            Root("root"){

                @Override
                protected <E> void enter(DefaultBehaviorTreeReader<E> reader, String name, boolean isGuard) {
                    reader.subtreeName = "";
                }

                @Override
                protected <E> boolean attribute(DefaultBehaviorTreeReader<E> reader, String name, Object value) {
                    reader.throwAttributeTypeException(this.name, name, null);
                    return true;
                }

                @Override
                protected <E> void exit(DefaultBehaviorTreeReader<E> reader) {
                    reader.switchToNewTree(reader.subtreeName);
                    reader.subtreeName = null;
                }
            }
            ,
            TreeTask(null){

                @Override
                protected <E> void enter(DefaultBehaviorTreeReader<E> reader, String name, boolean isGuard) {
                    if (reader.currentTree == null) {
                        reader.switchToNewTree("");
                        reader.subtreeName = null;
                    }
                    reader.openTask(name, isGuard);
                }

                @Override
                protected <E> boolean attribute(DefaultBehaviorTreeReader<E> reader, String name, Object value) {
                    StackedTask<E> stackedTask = reader.getCurrentTask();
                    AttrInfo ai = stackedTask.metadata.attributes.get(name);
                    if (ai == null) {
                        return false;
                    }
                    boolean isNew = reader.encounteredAttributes.add(name);
                    if (!isNew) {
                        throw reader.stackedTaskException(stackedTask, "attribute '" + name + "' specified more than once");
                    }
                    Field attributeField = reader.getField(stackedTask.task.getClass(), ai.fieldName);
                    reader.setField(attributeField, stackedTask.task, value);
                    return true;
                }

                @Override
                protected <E> void exit(DefaultBehaviorTreeReader<E> reader) {
                    if (!reader.isSubtreeRef) {
                        reader.checkRequiredAttributes(reader.getCurrentTask());
                        reader.encounteredAttributes.clear();
                    }
                }
            };
            
            String name;

            private Statement(String name) {
                this.name = name;
            }

            protected abstract <E> void enter(DefaultBehaviorTreeReader<E> var1, String var2, boolean var3);

            protected abstract <E> boolean attribute(DefaultBehaviorTreeReader<E> var1, String var2, Object var3);

            protected abstract <E> void exit(DefaultBehaviorTreeReader<E> var1);

        }

    }

}

