/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gokustudio.tentenbackground.parse;


/**
 *
 * @author Son Pham
 */
public class SourceEndpoint {
//
//    public static final String OBJECT_NAME = "source";
//    public static final String OBJECT_ID = "objectId";
//    public static final String ID = "souceId";
//    public static final String NAME = "name";
//    public static final String URL = "url";
//
//    public void add(Source source) throws ParseException {
//        ParseObject sourceObject = toParseObject(source);
//        sourceObject.save();
//    }
//
//    public List<Source> getAll() {
//        ParseQuery<ParseObject> query = ParseQuery.getQuery(OBJECT_NAME);
//        List<Source> listSources = new ArrayList<>();
//        try {
//            List<ParseObject> list = query.find();
//            for (ParseObject object : list) {
//                Source source = get(object);
//                listSources.add(source);
//            }
//        } catch (ParseException ex) {
//            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return listSources;
//    }
//
//    public Source get(String objectId) {
//        ParseQuery<ParseObject> query = ParseQuery.getQuery(OBJECT_NAME);
//        try {
//            ParseObject object = query.get(objectId);
//            if (object != null) {
//                Source source = get(object);
//                return source;
//            }
//        } catch (ParseException ex) {
//            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return null;
//    }
//
//    public Source getSourceByName(String name) {
//        ParseQuery<ParseObject> query = ParseQuery.getQuery(OBJECT_NAME);
//        try {
//            List<ParseObject> objects = query.whereEqualTo(NAME, name).find();
//            if (objects != null && objects.size() > 0) {
//                Source source = get(objects.get(0));
//                return source;
//            }
//        } catch (ParseException ex) {
//            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return null;
//    }
//
//    public Source getSourceById(String id) {
//        ParseQuery<ParseObject> query = ParseQuery.getQuery(OBJECT_NAME);
//        try {
//            List<ParseObject> objects = query.whereEqualTo(ID, id).find();
//            if (objects != null && objects.size() > 0) {
//                Source source = get(objects.get(0));
//                return source;
//            }
//        } catch (ParseException ex) {
//            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return null;
//    }
//
//    public static Source get(ParseObject sourceObject) {
//        String objectId = sourceObject.getObjectId();
//        String id = sourceObject.getString(ID);
//        String name = sourceObject.getString(NAME);
//        String source = sourceObject.getString(URL);
//        return new Source(objectId, id, name, source);
//    }
//
//    public static ParseObject toParseObject(Source source) {
//        ParseObject sourceObject = new ParseObject(OBJECT_NAME);
//        if (source.getObjectId() != null) {
//            sourceObject.setObjectId(source.getObjectId());
//        }
//        if (source.getUrl() != null) {
//            sourceObject.put(URL, source.getUrl());
//        }
//
//        sourceObject.put(ID, source.getId());
//        sourceObject.put(NAME, source.getName());
//        return sourceObject;
//    }
}
