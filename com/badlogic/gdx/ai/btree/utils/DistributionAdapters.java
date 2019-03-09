/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.btree.utils;

import com.badlogic.gdx.ai.utils.random.ConstantDoubleDistribution;
import com.badlogic.gdx.ai.utils.random.ConstantFloatDistribution;
import com.badlogic.gdx.ai.utils.random.ConstantIntegerDistribution;
import com.badlogic.gdx.ai.utils.random.ConstantLongDistribution;
import com.badlogic.gdx.ai.utils.random.Distribution;
import com.badlogic.gdx.ai.utils.random.DoubleDistribution;
import com.badlogic.gdx.ai.utils.random.FloatDistribution;
import com.badlogic.gdx.ai.utils.random.GaussianDoubleDistribution;
import com.badlogic.gdx.ai.utils.random.GaussianFloatDistribution;
import com.badlogic.gdx.ai.utils.random.IntegerDistribution;
import com.badlogic.gdx.ai.utils.random.LongDistribution;
import com.badlogic.gdx.ai.utils.random.TriangularDoubleDistribution;
import com.badlogic.gdx.ai.utils.random.TriangularFloatDistribution;
import com.badlogic.gdx.ai.utils.random.TriangularIntegerDistribution;
import com.badlogic.gdx.ai.utils.random.TriangularLongDistribution;
import com.badlogic.gdx.ai.utils.random.UniformDoubleDistribution;
import com.badlogic.gdx.ai.utils.random.UniformFloatDistribution;
import com.badlogic.gdx.ai.utils.random.UniformIntegerDistribution;
import com.badlogic.gdx.ai.utils.random.UniformLongDistribution;
import com.badlogic.gdx.utils.ObjectMap;
import java.util.StringTokenizer;

public class DistributionAdapters {
    private static final ObjectMap<Class<?>, Adapter<?>> adapters = new ObjectMap();
    ObjectMap<Class<?>, Adapter<?>> map = new ObjectMap();
    ObjectMap<Class<?>, ObjectMap<String, Adapter<?>>> typeMap = new ObjectMap();

    public DistributionAdapters() {
        for (ObjectMap.Entry e : adapters.entries()) {
            this.add((Class)e.key, (Adapter)e.value);
        }
    }

    public final void add(Class<?> clazz, Adapter<?> adapter) {
        this.map.put(clazz, adapter);
        ObjectMap m = this.typeMap.get(adapter.type);
        if (m == null) {
            m = new ObjectMap();
            this.typeMap.put(adapter.type, m);
        }
        m.put(adapter.category, adapter);
    }

    public <T extends Distribution> T toDistribution(String value, Class<T> clazz) {
        StringTokenizer st = new StringTokenizer(value, ", \t\f");
        if (!st.hasMoreTokens()) {
            throw new DistributionFormatException("Missing ditribution type");
        }
        String type = st.nextToken();
        ObjectMap categories = this.typeMap.get(clazz);
        Adapter converter = categories.get(type);
        if (converter == null) {
            throw new DistributionFormatException("Cannot create a '" + clazz.getSimpleName() + "' of type '" + type + "'");
        }
        String[] args = new String[st.countTokens()];
        for (int i = 0; i < args.length; ++i) {
            args[i] = st.nextToken();
        }
        return (T)converter.toDistribution(args);
    }

    public String toString(Distribution distribution) {
        Adapter adapter = this.map.get(distribution.getClass());
        String[] args = adapter.toParameters((Distribution)distribution);
        String out = adapter.category;
        for (String a : args) {
            out = out + "," + a;
        }
        return out;
    }

    private static /* varargs */ DistributionFormatException invalidNumberOfArgumentsException(int found, int ... expected) {
        String message = "Found " + found + " arguments in triangular distribution; expected ";
        if (expected.length < 2) {
            message = message + expected.length;
        } else {
            String sep = "";
            int i = 0;
            while (i < expected.length - 1) {
                message = message + sep + expected[i++];
                sep = ", ";
            }
            message = message + " or " + expected[i];
        }
        return new DistributionFormatException(message);
    }

    static {
        adapters.put(ConstantDoubleDistribution.class, ()new DoubleAdapter<ConstantDoubleDistribution>("constant"){

            @Override
            public ConstantDoubleDistribution toDistribution(String[] args) {
                if (args.length != 1) {
                    throw DistributionAdapters.invalidNumberOfArgumentsException(args.length, new int[]{1});
                }
                return new ConstantDoubleDistribution(.parseDouble(args[0]));
            }

            @Override
            public String[] toParameters(ConstantDoubleDistribution distribution) {
                return new String[]{Double.toString(distribution.getValue())};
            }
        });
        adapters.put(ConstantFloatDistribution.class, ()new FloatAdapter<ConstantFloatDistribution>("constant"){

            @Override
            public ConstantFloatDistribution toDistribution(String[] args) {
                if (args.length != 1) {
                    throw DistributionAdapters.invalidNumberOfArgumentsException(args.length, new int[]{1});
                }
                return new ConstantFloatDistribution(.parseFloat(args[0]));
            }

            @Override
            public String[] toParameters(ConstantFloatDistribution distribution) {
                return new String[]{Float.toString(distribution.getValue())};
            }
        });
        adapters.put(ConstantIntegerDistribution.class, ()new IntegerAdapter<ConstantIntegerDistribution>("constant"){

            @Override
            public ConstantIntegerDistribution toDistribution(String[] args) {
                if (args.length != 1) {
                    throw DistributionAdapters.invalidNumberOfArgumentsException(args.length, new int[]{1});
                }
                return new ConstantIntegerDistribution(.parseInteger(args[0]));
            }

            @Override
            public String[] toParameters(ConstantIntegerDistribution distribution) {
                return new String[]{Integer.toString(distribution.getValue())};
            }
        });
        adapters.put(ConstantLongDistribution.class, ()new LongAdapter<ConstantLongDistribution>("constant"){

            @Override
            public ConstantLongDistribution toDistribution(String[] args) {
                if (args.length != 1) {
                    throw DistributionAdapters.invalidNumberOfArgumentsException(args.length, new int[]{1});
                }
                return new ConstantLongDistribution(.parseLong(args[0]));
            }

            @Override
            public String[] toParameters(ConstantLongDistribution distribution) {
                return new String[]{Long.toString(distribution.getValue())};
            }
        });
        adapters.put(GaussianDoubleDistribution.class, ()new DoubleAdapter<GaussianDoubleDistribution>("gaussian"){

            @Override
            public GaussianDoubleDistribution toDistribution(String[] args) {
                if (args.length != 2) {
                    throw DistributionAdapters.invalidNumberOfArgumentsException(args.length, new int[]{2});
                }
                return new GaussianDoubleDistribution(.parseDouble(args[0]), .parseDouble(args[1]));
            }

            @Override
            public String[] toParameters(GaussianDoubleDistribution distribution) {
                return new String[]{Double.toString(distribution.getMean()), Double.toString(distribution.getStandardDeviation())};
            }
        });
        adapters.put(GaussianFloatDistribution.class, ()new FloatAdapter<GaussianFloatDistribution>("gaussian"){

            @Override
            public GaussianFloatDistribution toDistribution(String[] args) {
                if (args.length != 2) {
                    throw DistributionAdapters.invalidNumberOfArgumentsException(args.length, new int[]{2});
                }
                return new GaussianFloatDistribution(.parseFloat(args[0]), .parseFloat(args[1]));
            }

            @Override
            public String[] toParameters(GaussianFloatDistribution distribution) {
                return new String[]{Float.toString(distribution.getMean()), Float.toString(distribution.getStandardDeviation())};
            }
        });
        adapters.put(TriangularDoubleDistribution.class, ()new DoubleAdapter<TriangularDoubleDistribution>("triangular"){

            @Override
            public TriangularDoubleDistribution toDistribution(String[] args) {
                switch (args.length) {
                    case 1: {
                        return new TriangularDoubleDistribution(.parseDouble(args[0]));
                    }
                    case 2: {
                        return new TriangularDoubleDistribution(.parseDouble(args[0]), .parseDouble(args[1]));
                    }
                    case 3: {
                        return new TriangularDoubleDistribution(.parseDouble(args[0]), .parseDouble(args[1]), .parseDouble(args[2]));
                    }
                }
                throw DistributionAdapters.invalidNumberOfArgumentsException(args.length, new int[]{1, 2, 3});
            }

            @Override
            public String[] toParameters(TriangularDoubleDistribution distribution) {
                return new String[]{Double.toString(distribution.getLow()), Double.toString(distribution.getHigh()), Double.toString(distribution.getMode())};
            }
        });
        adapters.put(TriangularFloatDistribution.class, ()new FloatAdapter<TriangularFloatDistribution>("triangular"){

            @Override
            public TriangularFloatDistribution toDistribution(String[] args) {
                switch (args.length) {
                    case 1: {
                        return new TriangularFloatDistribution(.parseFloat(args[0]));
                    }
                    case 2: {
                        return new TriangularFloatDistribution(.parseFloat(args[0]), .parseFloat(args[1]));
                    }
                    case 3: {
                        return new TriangularFloatDistribution(.parseFloat(args[0]), .parseFloat(args[1]), .parseFloat(args[2]));
                    }
                }
                throw DistributionAdapters.invalidNumberOfArgumentsException(args.length, new int[]{1, 2, 3});
            }

            @Override
            public String[] toParameters(TriangularFloatDistribution distribution) {
                return new String[]{Float.toString(distribution.getLow()), Float.toString(distribution.getHigh()), Float.toString(distribution.getMode())};
            }
        });
        adapters.put(TriangularIntegerDistribution.class, ()new IntegerAdapter<TriangularIntegerDistribution>("triangular"){

            @Override
            public TriangularIntegerDistribution toDistribution(String[] args) {
                switch (args.length) {
                    case 1: {
                        return new TriangularIntegerDistribution(.parseInteger(args[0]));
                    }
                    case 2: {
                        return new TriangularIntegerDistribution(.parseInteger(args[0]), .parseInteger(args[1]));
                    }
                    case 3: {
                        return new TriangularIntegerDistribution(.parseInteger(args[0]), .parseInteger(args[1]), Float.valueOf(args[2]).floatValue());
                    }
                }
                throw DistributionAdapters.invalidNumberOfArgumentsException(args.length, new int[]{1, 2, 3});
            }

            @Override
            public String[] toParameters(TriangularIntegerDistribution distribution) {
                return new String[]{Integer.toString(distribution.getLow()), Integer.toString(distribution.getHigh()), Float.toString(distribution.getMode())};
            }
        });
        adapters.put(TriangularLongDistribution.class, ()new LongAdapter<TriangularLongDistribution>("triangular"){

            @Override
            public TriangularLongDistribution toDistribution(String[] args) {
                switch (args.length) {
                    case 1: {
                        return new TriangularLongDistribution(.parseLong(args[0]));
                    }
                    case 2: {
                        return new TriangularLongDistribution(.parseLong(args[0]), .parseLong(args[1]));
                    }
                    case 3: {
                        return new TriangularLongDistribution(.parseLong(args[0]), .parseLong(args[1]), .parseDouble(args[2]));
                    }
                }
                throw DistributionAdapters.invalidNumberOfArgumentsException(args.length, new int[]{1, 2, 3});
            }

            @Override
            public String[] toParameters(TriangularLongDistribution distribution) {
                return new String[]{Long.toString(distribution.getLow()), Long.toString(distribution.getHigh()), Double.toString(distribution.getMode())};
            }
        });
        adapters.put(UniformDoubleDistribution.class, ()new DoubleAdapter<UniformDoubleDistribution>("uniform"){

            @Override
            public UniformDoubleDistribution toDistribution(String[] args) {
                switch (args.length) {
                    case 1: {
                        return new UniformDoubleDistribution(.parseDouble(args[0]));
                    }
                    case 2: {
                        return new UniformDoubleDistribution(.parseDouble(args[0]), .parseDouble(args[1]));
                    }
                }
                throw DistributionAdapters.invalidNumberOfArgumentsException(args.length, new int[]{1, 2});
            }

            @Override
            public String[] toParameters(UniformDoubleDistribution distribution) {
                return new String[]{Double.toString(distribution.getLow()), Double.toString(distribution.getHigh())};
            }
        });
        adapters.put(UniformFloatDistribution.class, ()new FloatAdapter<UniformFloatDistribution>("uniform"){

            @Override
            public UniformFloatDistribution toDistribution(String[] args) {
                switch (args.length) {
                    case 1: {
                        return new UniformFloatDistribution(.parseFloat(args[0]));
                    }
                    case 2: {
                        return new UniformFloatDistribution(.parseFloat(args[0]), .parseFloat(args[1]));
                    }
                }
                throw DistributionAdapters.invalidNumberOfArgumentsException(args.length, new int[]{1, 2});
            }

            @Override
            public String[] toParameters(UniformFloatDistribution distribution) {
                return new String[]{Float.toString(distribution.getLow()), Float.toString(distribution.getHigh())};
            }
        });
        adapters.put(UniformIntegerDistribution.class, ()new IntegerAdapter<UniformIntegerDistribution>("uniform"){

            @Override
            public UniformIntegerDistribution toDistribution(String[] args) {
                switch (args.length) {
                    case 1: {
                        return new UniformIntegerDistribution(.parseInteger(args[0]));
                    }
                    case 2: {
                        return new UniformIntegerDistribution(.parseInteger(args[0]), .parseInteger(args[1]));
                    }
                }
                throw DistributionAdapters.invalidNumberOfArgumentsException(args.length, new int[]{1, 2});
            }

            @Override
            public String[] toParameters(UniformIntegerDistribution distribution) {
                return new String[]{Integer.toString(distribution.getLow()), Integer.toString(distribution.getHigh())};
            }
        });
        adapters.put(UniformLongDistribution.class, ()new LongAdapter<UniformLongDistribution>("uniform"){

            @Override
            public UniformLongDistribution toDistribution(String[] args) {
                switch (args.length) {
                    case 1: {
                        return new UniformLongDistribution(.parseLong(args[0]));
                    }
                    case 2: {
                        return new UniformLongDistribution(.parseLong(args[0]), .parseLong(args[1]));
                    }
                }
                throw DistributionAdapters.invalidNumberOfArgumentsException(args.length, new int[]{1, 2});
            }

            @Override
            public String[] toParameters(UniformLongDistribution distribution) {
                return new String[]{Long.toString(distribution.getLow()), Long.toString(distribution.getHigh())};
            }
        });
    }

    public static abstract class LongAdapter<D extends LongDistribution>
    extends Adapter<D> {
        public LongAdapter(String category) {
            super(category, LongDistribution.class);
        }
    }

    public static abstract class IntegerAdapter<D extends IntegerDistribution>
    extends Adapter<D> {
        public IntegerAdapter(String category) {
            super(category, IntegerDistribution.class);
        }
    }

    public static abstract class FloatAdapter<D extends FloatDistribution>
    extends Adapter<D> {
        public FloatAdapter(String category) {
            super(category, FloatDistribution.class);
        }
    }

    public static abstract class DoubleAdapter<D extends DoubleDistribution>
    extends Adapter<D> {
        public DoubleAdapter(String category) {
            super(category, DoubleDistribution.class);
        }
    }

    public static abstract class Adapter<D extends Distribution> {
        final String category;
        final Class<?> type;

        public Adapter(String category, Class<?> type) {
            this.category = category;
            this.type = type;
        }

        public abstract D toDistribution(String[] var1);

        public abstract String[] toParameters(D var1);

        public static double parseDouble(String v) {
            try {
                return Double.parseDouble(v);
            }
            catch (NumberFormatException nfe) {
                throw new DistributionFormatException("Not a double value: " + v, nfe);
            }
        }

        public static float parseFloat(String v) {
            try {
                return Float.parseFloat(v);
            }
            catch (NumberFormatException nfe) {
                throw new DistributionFormatException("Not a float value: " + v, nfe);
            }
        }

        public static int parseInteger(String v) {
            try {
                return Integer.parseInt(v);
            }
            catch (NumberFormatException nfe) {
                throw new DistributionFormatException("Not an int value: " + v, nfe);
            }
        }

        public static long parseLong(String v) {
            try {
                return Long.parseLong(v);
            }
            catch (NumberFormatException nfe) {
                throw new DistributionFormatException("Not a long value: " + v, nfe);
            }
        }
    }

    public static class DistributionFormatException
    extends RuntimeException {
        public DistributionFormatException() {
        }

        public DistributionFormatException(String s) {
            super(s);
        }

        public DistributionFormatException(String message, Throwable cause) {
            super(message, cause);
        }

        public DistributionFormatException(Throwable cause) {
            super(cause);
        }
    }

}

