/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gokustudio.tentenbackground.parse;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import gokustudio.tentenbackground.models.parse.Author;

/**
 * @author son
 */
public class AuthorEndpoint {

    public static final String OBJECT_NAME = "author";
    public static final String OBJECT_ID = "objectId";
    public static final String ID = "authorId";
    public static final String SOURCE_ID = "sourceId";
    public static final String NAME = "name";
    public static final String URL = "url";
    public static final String DESCRIPTION = "description";
    public static final String THUMBNAIL = "thumbnail";

    public static void saveAuthorToLocal(Author author) throws ParseException {
        ParseObject authorObject = toParseObject(author);
        authorObject.pin();
    }

    public List<Author> getAll() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(OBJECT_NAME);
        List<Author> listAuthors = new ArrayList<>();
        try {
            List<ParseObject> list = query.find();
            for (ParseObject object : list) {
                Author author = get(object);
                listAuthors.add(author);
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return listAuthors;
    }

    public Author get(String objectId) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(OBJECT_NAME);
        try {
            ParseObject object = query.get(objectId);
            if (object != null) {
                Author author = get(object);
                return author;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public Author getFromLocal(String objectId) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(OBJECT_NAME);
        query.fromLocalDatastore();
        try {
            ParseObject object = query.get(objectId);
            if (object != null) {
                Author author = get(object);
                return author;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public Author getAuthorByName(String name) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(OBJECT_NAME);
        try {
            List<ParseObject> objects = query.whereEqualTo(NAME, name).find();
            if (objects != null && objects.size() > 0) {
                Author author = get(objects.get(0));
                return author;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public Author getAuthorById(String id, String sourceId) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(OBJECT_NAME);
        try {
            List<ParseObject> objects = query.whereEqualTo(ID, id).whereEqualTo(SOURCE_ID, sourceId).find();
            if (objects != null && objects.size() > 0) {
                Author author = get(objects.get(0));
                return author;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public Author getAuthorByIdAndSource(String id, String source) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(OBJECT_NAME);
        try {
            List<ParseObject> objects = query.whereEqualTo(ID, id).whereEqualTo(SOURCE_ID, source).find();
            if (objects != null && objects.size() > 0) {
                Author author = get(objects.get(0));
                return author;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static Author get(ParseObject authorObject) {
        String objectId = authorObject.getObjectId();
        String id = authorObject.getString(ID);
        String source = authorObject.getString(SOURCE_ID);
        String name = authorObject.getString(NAME);
        String url = authorObject.getString(URL);
        String description = authorObject.getString(DESCRIPTION);
        String thumbnail = authorObject.getString(THUMBNAIL);
        return new Author(objectId, id, source, name, url, description, thumbnail);
    }

    public static ParseObject toParseObject(Author author) {
        ParseObject authorParseObject = new ParseObject(OBJECT_NAME);
        if (author.getObjectId() != null) {
            authorParseObject.setObjectId(author.getObjectId());
        }
        if (author.getUrl() != null) {
            authorParseObject.put(URL, author.getUrl());
        }
        if (author.getDescription() != null) {
            authorParseObject.put(DESCRIPTION, author.getDescription());
        }
        if (author.getThumbnail() != null) {
            authorParseObject.put(THUMBNAIL, author.getThumbnail());
        }
        authorParseObject.put(ID, author.getId());
        authorParseObject.put(SOURCE_ID, author.getSourceId());
        authorParseObject.put(NAME, author.getName());
        return authorParseObject;
    }

}
