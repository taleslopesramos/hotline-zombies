/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.maps.tiled;

import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.ImageResolver;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapImageLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSets;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.StreamUtils;
import com.badlogic.gdx.utils.XmlReader;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.StringTokenizer;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

public abstract class BaseTmxMapLoader<P extends AssetLoaderParameters<TiledMap>>
extends AsynchronousAssetLoader<TiledMap, P> {
    protected static final int FLAG_FLIP_HORIZONTALLY = Integer.MIN_VALUE;
    protected static final int FLAG_FLIP_VERTICALLY = 1073741824;
    protected static final int FLAG_FLIP_DIAGONALLY = 536870912;
    protected static final int MASK_CLEAR = -536870912;
    protected XmlReader xml = new XmlReader();
    protected XmlReader.Element root;
    protected boolean convertObjectToTileSpace;
    protected boolean flipY = true;
    protected int mapTileWidth;
    protected int mapTileHeight;
    protected int mapWidthInPixels;
    protected int mapHeightInPixels;
    protected TiledMap map;

    public BaseTmxMapLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    protected void loadTileLayer(TiledMap map, XmlReader.Element element) {
        if (element.getName().equals("layer")) {
            int width = element.getIntAttribute("width", 0);
            int height = element.getIntAttribute("height", 0);
            int tileWidth = element.getParent().getIntAttribute("tilewidth", 0);
            int tileHeight = element.getParent().getIntAttribute("tileheight", 0);
            TiledMapTileLayer layer = new TiledMapTileLayer(width, height, tileWidth, tileHeight);
            this.loadBasicLayerInfo(layer, element);
            int[] ids = BaseTmxMapLoader.getTileIds(element, width, height);
            TiledMapTileSets tilesets = map.getTileSets();
            for (int y = 0; y < height; ++y) {
                for (int x = 0; x < width; ++x) {
                    int id = ids[y * width + x];
                    boolean flipHorizontally = (id & Integer.MIN_VALUE) != 0;
                    boolean flipVertically = (id & 1073741824) != 0;
                    boolean flipDiagonally = (id & 536870912) != 0;
                    TiledMapTile tile = tilesets.getTile(id & 536870911);
                    if (tile == null) continue;
                    TiledMapTileLayer.Cell cell = this.createTileLayerCell(flipHorizontally, flipVertically, flipDiagonally);
                    cell.setTile(tile);
                    layer.setCell(x, this.flipY ? height - 1 - y : y, cell);
                }
            }
            XmlReader.Element properties = element.getChildByName("properties");
            if (properties != null) {
                this.loadProperties(layer.getProperties(), properties);
            }
            map.getLayers().add(layer);
        }
    }

    protected void loadObjectGroup(TiledMap map, XmlReader.Element element) {
        if (element.getName().equals("objectgroup")) {
            String name = element.getAttribute("name", null);
            MapLayer layer = new MapLayer();
            layer.setName(name);
            XmlReader.Element properties = element.getChildByName("properties");
            if (properties != null) {
                this.loadProperties(layer.getProperties(), properties);
            }
            for (XmlReader.Element objectElement : element.getChildrenByName("object")) {
                this.loadObject(map, layer, objectElement);
            }
            map.getLayers().add(layer);
        }
    }

    protected void loadImageLayer(TiledMap map, XmlReader.Element element, FileHandle tmxFile, ImageResolver imageResolver) {
        if (element.getName().equals("imagelayer")) {
            int x = Integer.parseInt(element.getAttribute("x", "0"));
            int y = Integer.parseInt(element.getAttribute("y", "0"));
            if (this.flipY) {
                y = this.mapHeightInPixels - y;
            }
            TextureRegion texture = null;
            XmlReader.Element image = element.getChildByName("image");
            if (image != null) {
                String source = image.getAttribute("source");
                FileHandle handle = BaseTmxMapLoader.getRelativeFileHandle(tmxFile, source);
                texture = imageResolver.getImage(handle.path());
                y -= texture.getRegionHeight();
            }
            TiledMapImageLayer layer = new TiledMapImageLayer(texture, x, y);
            this.loadBasicLayerInfo(layer, element);
            XmlReader.Element properties = element.getChildByName("properties");
            if (properties != null) {
                this.loadProperties(layer.getProperties(), properties);
            }
            map.getLayers().add(layer);
        }
    }

    protected void loadBasicLayerInfo(MapLayer layer, XmlReader.Element element) {
        String name = element.getAttribute("name", null);
        float opacity = Float.parseFloat(element.getAttribute("opacity", "1.0"));
        boolean visible = element.getIntAttribute("visible", 1) == 1;
        layer.setName(name);
        layer.setOpacity(opacity);
        layer.setVisible(visible);
    }

    protected void loadObject(TiledMap map, MapLayer layer, XmlReader.Element element) {
        if (element.getName().equals("object")) {
            int id;
            String type;
            MapObject object = null;
            float scaleX = this.convertObjectToTileSpace ? 1.0f / (float)this.mapTileWidth : 1.0f;
            float scaleY = this.convertObjectToTileSpace ? 1.0f / (float)this.mapTileHeight : 1.0f;
            float x = element.getFloatAttribute("x", 0.0f) * scaleX;
            float y = (this.flipY ? (float)this.mapHeightInPixels - element.getFloatAttribute("y", 0.0f) : element.getFloatAttribute("y", 0.0f)) * scaleY;
            float width = element.getFloatAttribute("width", 0.0f) * scaleX;
            float height = element.getFloatAttribute("height", 0.0f) * scaleY;
            if (element.getChildCount() > 0) {
                float[] vertices;
                String[] point;
                String[] points;
                XmlReader.Element child = null;
                child = element.getChildByName("polygon");
                if (child != null) {
                    points = child.getAttribute("points").split(" ");
                    vertices = new float[points.length * 2];
                    for (int i = 0; i < points.length; ++i) {
                        point = points[i].split(",");
                        vertices[i * 2] = Float.parseFloat(point[0]) * scaleX;
                        vertices[i * 2 + 1] = Float.parseFloat(point[1]) * scaleY * (float)(this.flipY ? -1 : 1);
                    }
                    Polygon polygon = new Polygon(vertices);
                    polygon.setPosition(x, y);
                    object = new PolygonMapObject(polygon);
                } else {
                    child = element.getChildByName("polyline");
                    if (child != null) {
                        points = child.getAttribute("points").split(" ");
                        vertices = new float[points.length * 2];
                        for (int i = 0; i < points.length; ++i) {
                            point = points[i].split(",");
                            vertices[i * 2] = Float.parseFloat(point[0]) * scaleX;
                            vertices[i * 2 + 1] = Float.parseFloat(point[1]) * scaleY * (float)(this.flipY ? -1 : 1);
                        }
                        Polyline polyline = new Polyline(vertices);
                        polyline.setPosition(x, y);
                        object = new PolylineMapObject(polyline);
                    } else {
                        child = element.getChildByName("ellipse");
                        if (child != null) {
                            object = new EllipseMapObject(x, this.flipY ? y - height : y, width, height);
                        }
                    }
                }
            }
            if (object == null) {
                String gid = null;
                gid = element.getAttribute("gid", null);
                if (gid != null) {
                    int id2 = (int)Long.parseLong(gid);
                    boolean flipHorizontally = (id2 & Integer.MIN_VALUE) != 0;
                    boolean flipVertically = (id2 & 1073741824) != 0;
                    TiledMapTile tile = map.getTileSets().getTile(id2 & 536870911);
                    TiledMapTileMapObject tiledMapTileMapObject = new TiledMapTileMapObject(tile, flipHorizontally, flipVertically);
                    TextureRegion textureRegion = tiledMapTileMapObject.getTextureRegion();
                    tiledMapTileMapObject.getProperties().put("gid", id2);
                    tiledMapTileMapObject.setX(x);
                    tiledMapTileMapObject.setY(this.flipY ? y : y - height);
                    float objectWidth = element.getFloatAttribute("width", textureRegion.getRegionWidth());
                    float objectHeight = element.getFloatAttribute("height", textureRegion.getRegionHeight());
                    tiledMapTileMapObject.setScaleX(scaleX * (objectWidth / (float)textureRegion.getRegionWidth()));
                    tiledMapTileMapObject.setScaleY(scaleY * (objectHeight / (float)textureRegion.getRegionHeight()));
                    tiledMapTileMapObject.setRotation(element.getFloatAttribute("rotation", 0.0f));
                    object = tiledMapTileMapObject;
                } else {
                    object = new RectangleMapObject(x, this.flipY ? y - height : y, width, height);
                }
            }
            object.setName(element.getAttribute("name", null));
            String rotation = element.getAttribute("rotation", null);
            if (rotation != null) {
                object.getProperties().put("rotation", Float.valueOf(Float.parseFloat(rotation)));
            }
            if ((type = element.getAttribute("type", null)) != null) {
                object.getProperties().put("type", type);
            }
            if ((id = element.getIntAttribute("id", 0)) != 0) {
                object.getProperties().put("id", id);
            }
            object.getProperties().put("x", Float.valueOf(x));
            if (object instanceof TiledMapTileMapObject) {
                object.getProperties().put("y", Float.valueOf(y));
            } else {
                object.getProperties().put("y", Float.valueOf(this.flipY ? y - height : y));
            }
            object.getProperties().put("width", Float.valueOf(width));
            object.getProperties().put("height", Float.valueOf(height));
            object.setVisible(element.getIntAttribute("visible", 1) == 1);
            XmlReader.Element properties = element.getChildByName("properties");
            if (properties != null) {
                this.loadProperties(object.getProperties(), properties);
            }
            layer.getObjects().add(object);
        }
    }

    protected void loadProperties(MapProperties properties, XmlReader.Element element) {
        if (element == null) {
            return;
        }
        if (element.getName().equals("properties")) {
            for (XmlReader.Element property : element.getChildrenByName("property")) {
                String name = property.getAttribute("name", null);
                String value = property.getAttribute("value", null);
                if (value == null) {
                    value = property.getText();
                }
                properties.put(name, value);
            }
        }
    }

    protected TiledMapTileLayer.Cell createTileLayerCell(boolean flipHorizontally, boolean flipVertically, boolean flipDiagonally) {
        TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
        if (flipDiagonally) {
            if (flipHorizontally && flipVertically) {
                cell.setFlipHorizontally(true);
                cell.setRotation(3);
            } else if (flipHorizontally) {
                cell.setRotation(3);
            } else if (flipVertically) {
                cell.setRotation(1);
            } else {
                cell.setFlipVertically(true);
                cell.setRotation(3);
            }
        } else {
            cell.setFlipHorizontally(flipHorizontally);
            cell.setFlipVertically(flipVertically);
        }
        return cell;
    }

    public static int[] getTileIds(XmlReader.Element element, int width, int height) {
        XmlReader.Element data = element.getChildByName("data");
        String encoding = data.getAttribute("encoding", null);
        if (encoding == null) {
            throw new GdxRuntimeException("Unsupported encoding (XML) for TMX Layer Data");
        }
        int[] ids = new int[width * height];
        if (encoding.equals("csv")) {
            String[] array = data.getText().split(",");
            for (int i = 0; i < array.length; ++i) {
                ids[i] = (int)Long.parseLong(array[i].trim());
            }
        } else if (encoding.equals("base64")) {
            InputStream is = null;
            try {
                String compression = data.getAttribute("compression", null);
                byte[] bytes = Base64Coder.decode(data.getText());
                if (compression == null) {
                    is = new ByteArrayInputStream(bytes);
                } else if (compression.equals("gzip")) {
                    is = new BufferedInputStream(new GZIPInputStream(new ByteArrayInputStream(bytes), bytes.length));
                } else if (compression.equals("zlib")) {
                    is = new BufferedInputStream(new InflaterInputStream(new ByteArrayInputStream(bytes)));
                } else {
                    throw new GdxRuntimeException("Unrecognised compression (" + compression + ") for TMX Layer Data");
                }
                byte[] temp = new byte[4];
                for (int y = 0; y < height; ++y) {
                    for (int x = 0; x < width; ++x) {
                        int curr;
                        int read;
                        for (read = is.read((byte[])temp); read < temp.length && (curr = is.read(temp, read, temp.length - read)) != -1; read += curr) {
                        }
                        if (read != temp.length) {
                            throw new GdxRuntimeException("Error Reading TMX Layer Data: Premature end of tile data");
                        }
                        ids[y * width + x] = BaseTmxMapLoader.unsignedByteToInt(temp[0]) | BaseTmxMapLoader.unsignedByteToInt(temp[1]) << 8 | BaseTmxMapLoader.unsignedByteToInt(temp[2]) << 16 | BaseTmxMapLoader.unsignedByteToInt(temp[3]) << 24;
                    }
                }
            }
            catch (IOException e) {
                throw new GdxRuntimeException("Error Reading TMX Layer Data - IOException: " + e.getMessage());
            }
            finally {
                StreamUtils.closeQuietly(is);
            }
        } else {
            throw new GdxRuntimeException("Unrecognised encoding (" + encoding + ") for TMX Layer Data");
        }
        return ids;
    }

    protected static int unsignedByteToInt(byte b) {
        return b & 255;
    }

    protected static FileHandle getRelativeFileHandle(FileHandle file, String path) {
        StringTokenizer tokenizer = new StringTokenizer(path, "\\/");
        FileHandle result = file.parent();
        while (tokenizer.hasMoreElements()) {
            String token = tokenizer.nextToken();
            if (token.equals("..")) {
                result = result.parent();
                continue;
            }
            result = result.child(token);
        }
        return result;
    }

    public static class Parameters
    extends AssetLoaderParameters<TiledMap> {
        public boolean generateMipMaps = false;
        public Texture.TextureFilter textureMinFilter = Texture.TextureFilter.Nearest;
        public Texture.TextureFilter textureMagFilter = Texture.TextureFilter.Nearest;
        public boolean convertObjectToTileSpace = false;
        public boolean flipY = true;
    }

}

