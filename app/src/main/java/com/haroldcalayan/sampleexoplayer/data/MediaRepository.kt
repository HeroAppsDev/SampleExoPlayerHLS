package com.haroldcalayan.sampleexoplayer.data

import android.net.Uri
import javax.inject.Inject

interface MediaRepository {
    suspend fun getMediaUri(): Uri
}

class MediaRepositoryImpl @Inject constructor(
   ) : MediaRepository {


    override suspend fun getMediaUri(): Uri {
        return Uri.parse("https://devstreaming-cdn.apple.com/videos/streaming/examples/img_bipbop_adv_example_ts/master.m3u8")
    }
}

