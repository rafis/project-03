package Util;

import eventb_prelude.BRelation;

/**
 * Created by me on 13.11.17.
 */

public class Wrappers {
    static public class BRelationII {
        public BRelation<Integer,Integer> data;

        public BRelationII(BRelation<Integer,Integer> data) {
            this.data = data;
        }
    }
}
