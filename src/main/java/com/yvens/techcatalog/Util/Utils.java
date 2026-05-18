package com.yvens.techcatalog.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.events.Event.ID;

import com.yvens.techcatalog.Entity.Product;
import com.yvens.techcatalog.Projection.IdProjection;
import com.yvens.techcatalog.Projection.ProductProjection;

public class Utils {

    public static <ID> List<? extends IdProjection<ID>> replace( List<? extends IdProjection<ID>>ordered,  List<? extends IdProjection<ID>>  unorded) {
          
        Map<ID, IdProjection<ID>> map =new HashMap<>();
        for( IdProjection<ID> obj: unorded){
            map.put(obj.getId(), obj);
        }
        List<IdProjection<ID>> result = new ArrayList<>();
        for(IdProjection<ID> obj: ordered){
            result.add(map.get(obj.getId()));
        }

        return result;
    }

}
