package com.weizu.sideslip

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*

/**
 * 侧滑菜单
 */
class MainActivity : AppCompatActivity() {

    val listView by lazy { findViewById<ListView>(R.id.listView) }
    val datas = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initDatas()
        listView.adapter = MyAdapter()
    }

    fun initDatas() {
        for (i in 0 until 30) {
            datas.add("Content ${i}")
        }
    }

    inner class MyAdapter : BaseAdapter() {
        override fun getCount() = datas.size
        override fun getItem(position: Int) = position
        override fun getItemId(position: Int) = position.toLong()
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var view: View? = convertView
            var myViewHolder: MyViewHolder? = null
            if (convertView == null) {
                myViewHolder = MyViewHolder()
                view = View.inflate(this@MainActivity, R.layout.item_layout, null)
                myViewHolder.textView = view.findViewById<TextView>(R.id.content_title)
                myViewHolder.delete = view.findViewById<TextView>(R.id.delete)
                view.setTag(myViewHolder)
            } else {
                myViewHolder = convertView.getTag() as MyViewHolder?
            }
            myViewHolder?.apply {
                myViewHolder.textView?.text = datas.get(position)
                (view as MyContainer).setOnItemChangeListener(MyItemMenuChangeListener())

                myViewHolder.textView?.setOnClickListener(object : View.OnClickListener {
                    override fun onClick(v: View?) {
                        Toast.makeText(
                            this@MainActivity,
                            "点击了：${datas.get(position)}",
                            android.widget.Toast.LENGTH_LONG
                        ).show()
                    }
                })

                // 删除数据，更新对应的ListView
                myViewHolder.delete?.setOnClickListener(object : View.OnClickListener {
                    override fun onClick(v: View?) {
                        // 由于ListView中Item的复用机制，会导致当前打开的Item用来显示下个数据，而实际上我们所
                        // 期望的是更新的时候，没有Item的Menu被打开，故而需要调用一次closeMenu
                        ((v?.parent?.parent) as MyContainer).closeMenu()
                        datas.remove(datas.get(position))
                        notifyDataSetChanged()
                    }
                })

            }
            return view!!
        }
    }

    inner class MyViewHolder {
        var textView: TextView? = null
        var delete: TextView? = null
    }

    // 在ListView中上一轮打开的Item
    var lastMyContainer: MyContainer? = null

    //
    inner class MyItemMenuChangeListener : MyContainer.OnItemMenuStateChangeListener {
        override fun onClose(view: MyContainer) {
            Log.e("TAG", "onClose: ")
            if (lastMyContainer == view) {
                lastMyContainer = null
            }
        }

        override fun onDown(view: MyContainer) {
            Log.e("TAG", "onDown: ")
            // 判断是否是本轮自己的MyContainer，否则就是上轮打开的MyContainer，也就是上轮的Item
            if (view != lastMyContainer) {
                lastMyContainer?.closeMenu()
            }
        }

        override fun onOpen(view: MyContainer) {
            Log.e("TAG", "onOpen: ")
            // 更新本轮MyContainer的值
            lastMyContainer = view
        }

    }
}