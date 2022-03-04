package com.weizu.contactindex

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import java.util.*
import kotlin.Comparator

/**
 * 联系人快速索引
 */
class MainActivity : AppCompatActivity() {

    val myIndex by lazy { findViewById<Index>(R.id.myIndex) }
    val listView by lazy { findViewById<ListView>(R.id.listView) }
    val tag by lazy { findViewById<TextView>(R.id.tag) }
    var datas = mutableListOf<String>("阿雷", "李四", "李思", "张晓飞", "胡继群",
        "刘畅", "尹革新", "温松", "李凤秋", "娄全超", "王英杰", "孙仁政", "姜宇航",
        "张洪瑞", "侯亚帅", "徐雨健", "阿三")
    lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initDatas()

        listView.adapter = MyAdapter()

        myIndex.setOnIndexTouchListener(object : Index.OnIndexTouchListener {
            override fun onIndexTouch(index: Int, word: String) {
                // 定位到listView的下标位置
                listView.setSelection(getIndex(word))
                // 显示一下标签Tag
                showTag(word)
            }
        })
    }

    fun showTag(word: String){
        tag.text = word
        tag.visibility = View.VISIBLE
        handler.removeCallbacksAndMessages(null)
        handler.postDelayed({
            tag.visibility = View.GONE
        }, 2000)
    }

    fun getIndex(word: String): Int{
        // 该下标为距离word最近的位置
        var itemIndex = 0
        for (i in 0 until datas.size){
            val current = PinYinUtils.getPinYin(datas.get(i)).subSequence(0, 1)
            if(word > current as String){
                itemIndex = i
            }
            if(current == word){
                itemIndex = i
                break
            }
        }
        return itemIndex
    }

    fun initDatas(){
        Collections.sort(datas, object : java.util.Comparator<String> {
            override fun compare(o1: String?, o2: String?): Int {
                return PinYinUtils.getPinYin(o1).compareTo(PinYinUtils.getPinYin(o2))
            }
        })

        handler = Handler(Looper.getMainLooper())
    }

    inner class MyAdapter: BaseAdapter() {
        override fun getCount() = datas.size
        override fun getItem(position: Int) = position
        override fun getItemId(position: Int) = position.toLong()

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var myViewHolder: MyViewHolder? = null
            var view: View? = convertView
            if(convertView == null){
                myViewHolder = MyViewHolder()
                view = View.inflate(this@MainActivity, R.layout.list_item, null)
                myViewHolder.top_char = view.findViewById<TextView>(R.id.top_char)
                myViewHolder.name = view.findViewById<TextView>(R.id.name)
            } else{
                myViewHolder = convertView.getTag() as MyViewHolder?
            }
            myViewHolder?.apply {
                val current = PinYinUtils.getPinYin(datas.get(position)).subSequence(0, 1)
                myViewHolder.top_char?.text = current
                myViewHolder.name?.text = datas.get(position)
                view?.setTag(myViewHolder)
                // 设置出现过的不再显示top_char
                if(position != 0 && PinYinUtils.getPinYin(datas.get(position - 1)).subSequence(0, 1).equals(current)){
                    myViewHolder.top_char?.visibility = View.GONE
                } else{
                    myViewHolder.top_char?.visibility = View.VISIBLE
                }
            }
            return view!!
        }
    }

    inner class MyViewHolder{
        var top_char: TextView? = null
        var name: TextView? = null
    }
}

