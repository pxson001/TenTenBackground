/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gokustudio.tentenbackground.parse;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.SaveCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import gokustudio.tentenbackground.callbacks.OnGetWallPaperFromLocalListener;
import gokustudio.tentenbackground.models.parse.Album;
import gokustudio.tentenbackground.models.parse.Author;
import gokustudio.tentenbackground.models.parse.Category;
import gokustudio.tentenbackground.models.parse.Tag;
import gokustudio.tentenbackground.models.parse.Wallpaper;


/**
 * @author son
 */
public class WallpaperEndpoint {

    public static final String OBJECT_NAME = "wallpaper";
    public static final String ID = "wallpaperId";
    public static final String NAME = "name";
    public static final String SOURCE_ID = "sourceId";
    public static final String CREATED_AT = "createdAt";
    public static final String NUM_OF_DOWNLOADS = "num_download";
    public static final String NUM_OF_FAVORITES = "num_favorite";
    public static final String NUM_OF_REPORTS = "num_report";
    public static final String COPYRIGHT_ID = "copyrightId";
    public static final String FILE_SIZE = "file_size";
    public static final String URL_1 = "url_1";
    public static final String URL_2 = "url_2";
    public static final String URL_3 = "url_3";
    public static final String URL_4 = "url_4";
    public static final String URL_5 = "url_5";
    public static final String TAGS = "tags";
    public static final String TAGS_JSON = "tagsJson";
    public static final String AUTHOR_JSON = "authorJson";


    // Local field
    public static final String IS_FAVORITE = "is_favorie";
    public static final String DOWNLOAD_PATH = "download_path";

    private static Gson gson;

    private static WallpaperTagEndpoint wallpaperTagEndpoint;
    private static AuthorEndpoint authorEndpoint;
    private static AlbumEndpoint albumEndpoint;
    private static CategoryEndpoint categoryEndpoint;
    private static TagEndpoint tagEndpoint;

    private OnGetWallPaperFromLocalListener mOnGetWallPaperFromLocalListener;

    public WallpaperEndpoint() {
        wallpaperTagEndpoint = new WallpaperTagEndpoint();
        authorEndpoint = new AuthorEndpoint();
        albumEndpoint = new AlbumEndpoint();
        categoryEndpoint = new CategoryEndpoint();
        tagEndpoint = new TagEndpoint();
        gson = new Gson();
    }



    public List<Wallpaper> getAllRecent(int skip, int limit) throws ParseException {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(OBJECT_NAME);
        query.setSkip(skip);
        query.setLimit(limit);
        query.orderByDescending(CREATED_AT);
        List<Wallpaper> listWallpapers = new ArrayList<>();
        List<ParseObject> parseObjects = query.find();

        if (parseObjects != null && parseObjects.size() > 0)
            for (ParseObject object : parseObjects) {
                try {
                    Wallpaper wallpaper = get(object);
                    listWallpapers.add(wallpaper);
                } catch (ParseException ex) {
                    ex.printStackTrace();
                    continue;
                }
            }
        return listWallpapers;

    }

    public List<Wallpaper> getByCategory(String categoryObjectId, int skip, int limit) throws ParseException {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(OBJECT_NAME);
        query.setSkip(skip);
        query.setLimit(limit);
        query.whereEqualTo(CategoryEndpoint.OBJECT_NAME, categoryObjectId);
        query.orderByDescending(CREATED_AT);
        List<Wallpaper> listWallpapers = new ArrayList<>();
        List<ParseObject> parseObjects = query.find();

        if (parseObjects != null && parseObjects.size() > 0)
            for (ParseObject object : parseObjects) {
                try {
                    Wallpaper wallpaper = get(object);
                    listWallpapers.add(wallpaper);
                } catch (ParseException ex) {
                    ex.printStackTrace();
                    continue;
                }
            }
        return listWallpapers;

    }

    public List<Wallpaper> getByTag(String tagObjectId, int skip, int limit) throws ParseException {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(OBJECT_NAME);
        query.setSkip(skip);
        query.setLimit(limit);
        query.whereMatches(TAGS_JSON, tagObjectId);
        List<Wallpaper> listWallpapers = new ArrayList<>();
        List<ParseObject> parseObjects = query.find();

        if (parseObjects != null && parseObjects.size() > 0)
            for (ParseObject object : parseObjects) {
                try {
                    Wallpaper wallpaper = get(object);
                    listWallpapers.add(wallpaper);
                } catch (ParseException ex) {
                    ex.printStackTrace();
                    continue;
                }
            }
        return listWallpapers;

    }

    public Wallpaper get(Wallpaper wallpaper) throws ParseException {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(OBJECT_NAME);
        ParseObject object = query.get(wallpaper.getObjectId());
        if (object != null) {
            try {
                Author author = getAuthorRelation(object);
                Album album = getAlbumRelation(object);
                Category category = getCategoryRelation(object);
                List<Tag> tags = wallpaperTagEndpoint.getTags(wallpaper.getObjectId());

                wallpaper.setAuthor(author);
                wallpaper.setCategory(category);
                wallpaper.setAlbum(album);
                wallpaper.setTags(tags);
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
        }
        return wallpaper;
    }


    public static Wallpaper get(ParseObject wallpaperObject) throws ParseException {
        String objectId = wallpaperObject.getObjectId();
        String id = wallpaperObject.getString(ID);
        String sourceId = wallpaperObject.getString(SOURCE_ID);
        String copyrightId = wallpaperObject.getString(COPYRIGHT_ID);
        String name = wallpaperObject.getString(NAME);
        String createdAt = wallpaperObject.getCreatedAt().toString();
        long numOfDownloads = wallpaperObject.getLong(NUM_OF_DOWNLOADS);
        long numOfFavorites = wallpaperObject.getLong(NUM_OF_FAVORITES);
        long numOfReports = wallpaperObject.getLong(NUM_OF_REPORTS);
        long fileSize = wallpaperObject.getLong(FILE_SIZE);
        String url1 = wallpaperObject.getString(URL_1);
        String url2 = wallpaperObject.getString(URL_2);
        String url3 = wallpaperObject.getString(URL_3);
        String url4 = wallpaperObject.getString(URL_4);
        String url5 = wallpaperObject.getString(URL_5);

        String tagsJson = wallpaperObject.getString(TAGS_JSON);
        Type listTagsType = new TypeToken<Tag[]>() {
        }.getType();
        Tag[] tagArray = gson.fromJson(tagsJson, listTagsType);
        List<Tag> tags = Arrays.asList(tagArray);

        String authorJson = wallpaperObject.getString(AUTHOR_JSON);
        Author author = gson.fromJson(authorJson, Author.class);

        return new Wallpaper(objectId, id, sourceId, name, createdAt, numOfDownloads, numOfFavorites, numOfReports, copyrightId, fileSize, url1, url2, url3, url4, url5, author, null, null, tags);
    }

    public Author getAuthorRelation(ParseObject parseObject) throws ParseException {
        ParseQuery<ParseObject> authorObject = parseObject.getRelation(AuthorEndpoint.OBJECT_NAME).getQuery();
        List<ParseObject> authors = authorObject.find();
        Author author = null;
        if (authors != null && authors.size() > 0) {
            author = AuthorEndpoint.get(authors.get(0));
        }
        return author;
    }

    public Author getAuthorRelationFromLocal(ParseObject parseObject) throws ParseException {
        ParseQuery<ParseObject> authorObject = parseObject.getRelation(AuthorEndpoint.OBJECT_NAME).getQuery().fromLocalDatastore();
        List<ParseObject> authors = authorObject.find();
        Author author = null;
        if (authors != null && authors.size() > 0) {
            author = AuthorEndpoint.get(authors.get(0));
        }
        return author;
    }

    public Album getAlbumRelation(ParseObject parseObject) throws ParseException {
        ParseQuery<ParseObject> albumObject = parseObject.getRelation(AlbumEndpoint.OBJECT_NAME).getQuery();
        List<ParseObject> albums = albumObject.find();
        Album album = null;
        if (albums != null && albums.size() > 0) {
            album = AlbumEndpoint.get(albums.get(0));
        }
        return album;
    }

    public Album getAlbumRelationFromLocal(ParseObject parseObject) throws ParseException {
        ParseQuery<ParseObject> albumObject = parseObject.getRelation(AlbumEndpoint.OBJECT_NAME).getQuery().fromLocalDatastore();
        List<ParseObject> albums = albumObject.find();
        Album album = null;
        if (albums != null && albums.size() > 0) {
            album = AlbumEndpoint.get(albums.get(0));
        }
        return album;
    }

    public Category getCategoryRelation(ParseObject parseObject) throws ParseException {
        ParseQuery<ParseObject> albumObject = parseObject.getRelation(CategoryEndpoint.OBJECT_NAME).getQuery();
        List<ParseObject> categories = albumObject.find();
        Category category = null;
        if (categories != null && categories.size() > 0) {
            category = CategoryEndpoint.get(categories.get(0));
        }
        return category;
    }

    public Category getCategoryRelationFromLocal(ParseObject parseObject) throws ParseException {
        ParseQuery<ParseObject> albumObject = parseObject.getRelation(CategoryEndpoint.OBJECT_NAME).getQuery().fromLocalDatastore();
        List<ParseObject> categories = albumObject.find();
        Category category = null;
        if (categories != null && categories.size() > 0) {
            category = CategoryEndpoint.get(categories.get(0));
        }
        return category;
    }


    public static CategoryEndpoint getCategoryEndpoint() {
        return categoryEndpoint;
    }

    public static ParseObject toParseObject(Wallpaper wallpaper) {
        ParseObject wallpaperObject = new ParseObject(OBJECT_NAME);
        wallpaperObject.put(NAME, wallpaper.getName());
        wallpaperObject.put(NUM_OF_DOWNLOADS, wallpaper.getNumOfDownloads());
        wallpaperObject.put(NUM_OF_FAVORITES, wallpaper.getNumOfFavorites());
        wallpaperObject.put(FILE_SIZE, wallpaper.getFileSize());
        wallpaperObject.put(URL_1, wallpaper.getUrl_1());
        if (wallpaper.getUrl_2() != null) {
            wallpaperObject.put(URL_2, wallpaper.getUrl_2());
        }
        if (wallpaper.getUrl_3() != null) {
            wallpaperObject.put(URL_3, wallpaper.getUrl_3());
        }
        if (wallpaper.getUrl_4() != null) {
            wallpaperObject.put(URL_4, wallpaper.getUrl_4());
        }
        if (wallpaper.getUrl_5() != null) {
            wallpaperObject.put(URL_5, wallpaper.getUrl_5());
        }
        ParseRelation<ParseObject> authorRelations = wallpaperObject.getRelation(AuthorEndpoint.OBJECT_NAME);
        authorRelations.add(ParseObject.createWithoutData(AuthorEndpoint.OBJECT_NAME, wallpaper.getAuthor().getId()));
        if (wallpaper.getAlbum() != null) {
            ParseRelation<ParseObject> albumRelations = wallpaperObject.getRelation(AlbumEndpoint.OBJECT_NAME);
            albumRelations.add(ParseObject.createWithoutData(AlbumEndpoint.OBJECT_NAME, wallpaper.getAlbum().getId()));
        }
        if (wallpaper.getCategory() != null) {
            ParseRelation<ParseObject> categoryRelations = wallpaperObject.getRelation(CategoryEndpoint.OBJECT_NAME);
            categoryRelations.add(ParseObject.createWithoutData(CategoryEndpoint.OBJECT_NAME, wallpaper.getCategory().getId()));
        }
        return wallpaperObject;
    }

    public void setOnGetWallPaperFromLocalListener(OnGetWallPaperFromLocalListener mOnGetWallPaperFromLocalListener) {
        this.mOnGetWallPaperFromLocalListener = mOnGetWallPaperFromLocalListener;
    }
}
