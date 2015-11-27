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

import gokustudio.tentenbackground.models.parse.Category;

/**
 *
 * @author son
 */
public class CategoryEndpoint {

    public static final String OBJECT_NAME = "category";
    public static final String ID = "categoryId";
    public static final String NAME = "name";
    public static final String THUMBNAIL = "thumbnail";
    public static final String COUNT = "count";

    public static void saveCategoryToLocal(Category category) throws ParseException {
        ParseObject categoryObject = toParseObject(category);
        categoryObject.pin();
    }

    public List<Category> getAll() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(OBJECT_NAME);
        List<Category> listCategories = new ArrayList<>();
        try {
            List<ParseObject> list = query.find();
            for (ParseObject object : list) {
                Category category = get(object);
                listCategories.add(category);
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return listCategories;
    }

    public Category get(String objectId) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(OBJECT_NAME);
        try {
            ParseObject object = query.get(objectId);
            if (object != null) {
                Category category = get(object);
                return category;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public Category getFromLocal(String objectId) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(OBJECT_NAME);
        query.fromLocalDatastore();
        try {
            ParseObject object = query.get(objectId);
            if (object != null) {
                Category category = get(object);
                return category;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public Category getCategoryByName(String name) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(OBJECT_NAME);
        try {
            List<ParseObject> objects = query.whereEqualTo(NAME, name).find();
            if (objects != null && objects.size() > 0) {
                Category category = get(objects.get(0));
                return category;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public Category getCategoryById(String id) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(OBJECT_NAME);
        try {
            List<ParseObject> objects = query.whereEqualTo(ID, id).find();
            if (objects != null && objects.size() > 0) {
                Category category = get(objects.get(0));
                return category;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public void update(Category category, int count) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(OBJECT_NAME);
        try {
            ParseObject parseObject = query.get(category.getObjectId());
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

    public static Category get(ParseObject categoryObject) {
        String objectId = categoryObject.getObjectId();
        String id = categoryObject.getString(ID);
        String name = categoryObject.getString(NAME);
        String thumbnail = categoryObject.getString(THUMBNAIL);
        int count = categoryObject.getInt(COUNT);
        return new Category(objectId, id, name, thumbnail,count);
    }

    public static ParseObject toParseObject(Category category) {
        ParseObject categoryObject = new ParseObject(OBJECT_NAME);
        if (category.getObjectId() != null) {
            categoryObject.setObjectId(category.getObjectId());
        }
        if (category.getThumbnail() != null) {
            categoryObject.put(THUMBNAIL, category.getThumbnail());
        }

        categoryObject.put(ID, category.getId());
        categoryObject.put(NAME, category.getName());
        return categoryObject;
    }


}
