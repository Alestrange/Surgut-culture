package ru.alestrange.cultureSurgut

import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import ru.alestrange.cultureSurgut.databinding.ActivityMainBinding
import ru.alestrange.cultureSurgut.databinding.ActivityObjectDetailBinding

private lateinit var binding: ActivityObjectDetailBinding

class ObjectDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityObjectDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val objectId = intent?.extras?.getInt("objectId")
        objectId?.let {
            val cultobject = SurgutCultureApplication.db.cultobjectDao().getCultobjectById(objectId)
            binding.textObjectName.text = cultobject.name
            binding.textObjectDescription.text = cultobject.description
            val bm = BitmapFactory.decodeFile("${applicationContext.filesDir}/$imagePath/${cultobject.image}.png")
            Log.i("mymy","result img ${bm?.width} ${bm?.height}")
            val d: Drawable = BitmapDrawable(applicationContext.resources, bm)
            binding.imageObject?.setImageDrawable(d)
        }
    }
}