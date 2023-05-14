package com.example.meal_preparation_application.utils


import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.meal_preparation_application.R

//source - https://www.geeksforgeeks.org/android-gridview-in-kotlin/

internal class GridAdeptor(

    private val courseList: List<GridViewMealModal>,
    private val context: Context
) :
    BaseAdapter() {

    private var layoutInflater: LayoutInflater? = null
    private lateinit var minicard_name: TextView
    private lateinit var minicard_category: TextView
    private lateinit var minicard_image: LinearLayout

    // below method is use to return the count of course list
    override fun getCount(): Int {
        return courseList.size
    }

    // below function is use to return the item of grid view.
    override fun getItem(position: Int): Any? {
        return null
    }

    // below function is use to return item id of grid view.
    override fun getItemId(position: Int): Long {
        return 0
    }

    // in below function we are getting individual item of grid view.
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var contentView = convertView
        // on blow line we are checking if layout inflater
        // is null, if it is null we are initializing it.
        if (layoutInflater == null) {
            layoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }

        // If it is null we are initializing it.
        if (contentView == null) {
            // on below line we are passing the layout file
            contentView = layoutInflater!!.inflate(R.layout.minicard_view, null)
        }
        // on below line we are initializing
        minicard_image = contentView!!.findViewById(R.id.idIVCourse)
        minicard_name = contentView.findViewById(R.id.meal_name)
        minicard_category = contentView.findViewById(R.id.meal_cat)


        minicard_name.setText(courseList[position].meal.name)
        minicard_category.setText(courseList[position].meal.category)
        // loading image
        var bitmapDrawable: BitmapDrawable? = null
        Glide.with(contentView)
            .asBitmap()
            .load(courseList[position].meal.mealThumb)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                    val drawable = BitmapDrawable(context.resources, resource)
                    // Create a new BitmapDrawable with the copied bitmap
                    courseList[position].bitmapDrawable =  BitmapDrawable(context.resources, drawable.bitmap.copy(Bitmap.Config.ARGB_8888, true))
                    drawable.alpha = 140
                    minicard_image.background = drawable
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    // this is called when imageView is cleared on lifecycle call or for
                    // some other reason.
                    // if you are referencing the bitmap somewhere else too other than this imageView
                    // clear it here as you can no longer have the bitmap
                }
            })
        return contentView
    }
}