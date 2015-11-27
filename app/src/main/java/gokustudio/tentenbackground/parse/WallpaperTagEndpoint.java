/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gokustudio.tentenbackground.parse;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import junit.framework.Test;

import gokustudio.tentenbackground.models.parse.Tag;
import gokustudio.tentenbackground.models.parse.Wallpaper;
import gokustudio.tentenbackground.models.parse.WallpaperTag;

/**
 *
 * @author son
 */
public class WallpaperTagEndpoint {

    public static final String OBJECT_NAME = "wallpaper_tag";
    public static final String ID = "objectId";

    public WallpaperTagEndpoint() {
    }


    public List<Tag> getTags(String wallpaperId) {
        List<Tag> listTags = new ArrayList<>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery(OBJECT_NAME);
        query.whereEqualTo(WallpaperEndpoint.OBJECT_NAME, ParseObject.createWithoutData(WallpaperEndpoint.OBJECT_NAME, wallpaperId));

        try {
            List<ParseObject> listWallpaperTagObjects = query.find();
            if (listWallpaperTagObjects != null && listWallpaperTagObjects.size() > 0) {
//                ParseObject.saveAllInBackground(listWallpaperTagObjects); // cache result

                for (ParseObject wallpaperTagObject : listWallpaperTagObjects) {
                    WallpaperTag wallpaperTag = get(wallpaperTagObject);
                    listTags.add(wallpaperTag.getTag());
                }
            }
        } catch (ParseException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listTags;
    }

    public List<Tag> getTagsFromLocal(String wallpaperId) {
        List<Tag> listTags = new ArrayList<>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery(OBJECT_NAME);
        query.fromLocalDatastore();
        query.whereEqualTo(WallpaperEndpoint.OBJECT_NAME, ParseObject.createWithoutData(WallpaperEndpoint.OBJECT_NAME, wallpaperId));

        try {
            List<ParseObject> listWallpaperTagObjects = query.find();
            if (listWallpaperTagObjects != null && listWallpaperTagObjects.size() > 0) {
                for (ParseObject wallpaperTagObject : listWallpaperTagObjects) {
                    WallpaperTag wallpaperTag = get(wallpaperTagObject);
                    listTags.add(wallpaperTag.getTag());
                }
            }
        } catch (ParseException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listTags;
    }


    public static WallpaperTag get(ParseObject wallpaperTagObject) throws ParseException {
        String id = wallpaperTagObject.getObjectId();

        ParseQuery<ParseObject> wallpaperQuery = wallpaperTagObject.getRelation(WallpaperEndpoint.OBJECT_NAME).getQuery();
        List<ParseObject> wallpapers = wallpaperQuery.find();
        Wallpaper wallpaper = null;
        if (wallpapers != null && wallpapers.size() > 0) {
            wallpaper = WallpaperEndpoint.get(wallpapers.get(0));
        }

        ParseQuery<ParseObject> tagQuery = wallpaperTagObject.getRelation(TagEndpoint.OBJECT_NAME).getQuery();
        List<ParseObject> tags = tagQuery.find();
        Tag tag = null;
        if (tags != null && tags.size() > 0) {
            tag = TagEndpoint.get(tags.get(0));
        }
        return new WallpaperTag(id, wallpaper, tag);
    }

    public static ParseObject toParseObject(WallpaperTag wallpaperTag) {
        ParseObject wallpaperTagObject = new ParseObject(OBJECT_NAME);
        Wallpaper wallpaper = wallpaperTag.getWallpaper();
        Tag tag = wallpaperTag.getTag();
        ParseRelation<ParseObject> wallpaperRelations = wallpaperTagObject.getRelation(WallpaperEndpoint.OBJECT_NAME);
        wallpaperRelations.add(ParseObject.createWithoutData(WallpaperEndpoint.OBJECT_NAME, wallpaper.getObjectId()));
        ParseRelation<ParseObject> tagRelations = wallpaperTagObject.getRelation(TagEndpoint.OBJECT_NAME);
        tagRelations.add(ParseObject.createWithoutData(TagEndpoint.OBJECT_NAME, tag.getObjectId()));
        return wallpaperTagObject;
    }


}
