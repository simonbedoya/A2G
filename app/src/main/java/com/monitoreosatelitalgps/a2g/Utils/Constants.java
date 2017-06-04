package com.monitoreosatelitalgps.a2g.Utils;

import com.monitoreosatelitalgps.a2g.Activity.MainActivity;
import com.monitoreosatelitalgps.a2g.Fragment.MapFragment;

/**
 * Created by sbv23 on 5/05/2017.
 */

public class Constants {

    public static class tabs{
        public static final int TAB_DETAIL = 0;
        public static final int TAB_MAP = 1;
        public static final int TAB_PROFILE = 2;
    }

    public static class tags{
        public static final String TAG_MAIN = MainActivity.class.getSimpleName();
        public static final String TAG_MAP = MapFragment.class.getSimpleName();
    }
}