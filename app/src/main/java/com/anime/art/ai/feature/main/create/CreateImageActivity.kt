package com.anime.art.ai.feature.main.create

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.anime.art.ai.R
import com.anime.art.ai.databinding.ActivityCreateImageBinding
import com.basic.common.base.LsActivity

class CreateImageActivity : LsActivity<ActivityCreateImageBinding>(ActivityCreateImageBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_image)
    }
}