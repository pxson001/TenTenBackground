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

import gokustudio.tentenbackground.models.parse.Tag;

/**
 * @author son
 */
public class TagEndpoint {

    public static final String OBJECT_NAME = "tag";
    public static final String OBJECT_ID = "objectId";
    public static final String ID = "tagId";
    public static final String NAME = "name";
    public static final String COUNT = "count";

    public static void saveTagToLocal(Tag tag) throws ParseException {
        ParseObject tagObject = toParseObject(tag);
        tagObject.pin();
    }

    public List<Tag> getAll() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(OBJECT_NAME);
        List<Tag> listTags = new ArrayList<>();
        try {
            List<ParseObject> list = query.find();
            for (ParseObject object : list) {
                Tag tag = get(object);
                listTags.add(tag);
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return listTags;
    }

    public Tag getFromLocal(String objectId) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(OBJECT_NAME);
        query.fromLocalDatastore();
        try {
            ParseObject object = query.get(objectId);
            if (object != null) {
                Tag tag = get(object);
                return tag;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();

        }
        return null;
    }

    public Tag get(String objectId) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(OBJECT_NAME);
        try {
            ParseObject object = query.get(objectId);
            if (object != null) {
                Tag tag = get(object);
                return tag;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();

        }
        return null;
    }

    public Tag getTagByName(String name) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(OBJECT_NAME);
        try {
            List<ParseObject> objects = query.whereEqualTo(NAME, name).find();
            if (objects != null && objects.size() > 0) {
                Tag tag = get(objects.get(0));
                return tag;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();

        }
        return null;
    }

    public Tag getTagById(String id) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(OBJECT_NAME);
        try {
            List<ParseObject> objects = query.whereEqualTo(ID, id).find();
            if (objects != null && objects.size() > 0) {
                Tag tag = get(objects.get(0));
                return tag;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();

        }
        return null;
    }

    public void update(Tag tag, int count) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(OBJECT_NAME);
        try {
            ParseObject parseObject = query.get(tag.getObjectId());
            if (parseObject != null) {
                parseObject.put(COUNT, count);
                parseObject.save();
            }
        } catch (ParseException ex) {
            ex.printStackTrace();

        }
    }

    public void update(String objectId, int count) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(OBJECT_NAME);
        try {
            ParseObject parseObject = query.get(objectId);
            if (parseObject != null) {
                parseObject.put(COUNT, count);
                parseObject.save();
            }
        } catch (ParseException ex) {
            ex.printStackTrace();

        }
    }

    public List<Tag> findTag(String strQuery, int limit) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(OBJECT_NAME);
        query.setLimit(limit);
        query.whereMatches(NAME, "(" + strQuery + ")", "i");
        List<Tag> listTags = new ArrayList<>();
        try {
            List<ParseObject> list = query.find();
            for (ParseObject object : list) {
                Tag tag = get(object);
                listTags.add(tag);
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return listTags;
    }

    public static Tag get(ParseObject tagObject) {
        String objectId = tagObject.getObjectId();
        String id = tagObject.getString(ID);
        String name = tagObject.getString(NAME);
        int count = tagObject.getInt(COUNT);
        return new Tag(objectId, id, name, count);
    }

    public static ParseObject toParseObject(Tag tag) {
        ParseObject tagObject = new ParseObject(OBJECT_NAME);
        if (tag.getId() != null) {
            tagObject.put(ID, tag.getId());
        }
        tagObject.put(NAME, tag.getName());
        return tagObject;
    }
}
