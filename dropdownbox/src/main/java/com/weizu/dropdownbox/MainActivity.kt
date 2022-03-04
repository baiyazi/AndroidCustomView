package com.weizu.dropdownbox

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*

/**
 * 下拉框
 */
class MainActivity : AppCompatActivity() {
    val editText by lazy {findViewById<EditText>(R.id.editText)}
    val imageview by lazy {findViewById<ImageView>(R.id.imageview)}
    var listView: ListView? = null
    var popupWindow: PopupWindow? = null
    val datas = listOf("123", "测试", "123", "测试", "123", "测试")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = ListView(this)
        listView?.adapter = MyListViewAdapter(this, R.layout.item, datas.toMutableList())

        imageview.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                // 创建PopupWindow
                if(popupWindow == null){
                    popupWindow = PopupWindow()
                    popupWindow?.width = editText.width
                    popupWindow?.height = dp2pix(200f)

                    popupWindow?.contentView = listView
                    popupWindow?.isFocusable = true  // 设置焦点，系统就会把各种事件交给popup来处理
                }
                popupWindow?.showAsDropDown(editText, 0, 0)
            }
        })

        listView?.setOnItemClickListener(object :AdapterView.OnItemClickListener{
            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                editText.setText(datas[position])

                // 关闭窗口
                popupWindow?.apply {
                    if(isShowing){
                        popupWindow?.dismiss()
                        popupWindow = null
                    }
                }
            }
        })
    }

    fun dp2pix(value: Float): Int{
        return (resources.displayMetrics.density * value).toInt()
    }
}

class MyListViewAdapter(context: Context, resource: Int, datas: MutableList<String>): BaseAdapter(){

    var mCtx: Context = context
    var mDatas: MutableList<String> = datas
    var mResId: Int = resource

    override fun getCount(): Int {
        return mDatas.size
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view: MyViewHolder? = null
        var tempView: View? = null
        if(convertView == null){
            tempView = View.inflate(mCtx, mResId, null) as View
            view = MyViewHolder()
            view.title = tempView.findViewById<TextView>(R.id.textView)
            view.delete = tempView.findViewById<ImageView>(R.id.delete)
        } else {
            tempView = convertView
            view = tempView.getTag() as MyViewHolder
        }
        // 设置标题
        val msg = mDatas.get(position)
        view.title?.text = msg

        // 设置删除按钮监听
        view.delete?.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                mDatas.remove(msg)
                // 通知数据发生改变
                notifyDataSetChanged()
            }
        })
        // 缓存实例化的view
        tempView.setTag(view)
        return tempView
    }

    inner class MyViewHolder{
        var delete: ImageView? = null
        var title: TextView? = null
    }
}