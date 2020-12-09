package com.esprit.jobhunter.MessengerFragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.esprit.jobhunter.Entity.Message
import com.esprit.jobhunter.Entity.User
import com.esprit.jobhunter.MyUtils.GlobalParams
import com.esprit.jobhunter.R
import com.esprit.jobhunter.RecyclerViewsAdapters.ChatBoxAdapter
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.net.URISyntaxException
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class ChatBoxFragment : Fragment() {
    var nickname: String? = null
    var connectedUser: User? = null
    var receiverUser1: User? = null
    private var socket: Socket? = null
    private val Nickname = ""
    var myRecylerView: RecyclerView? = null
    var MessageList: MutableList<Message>? = null
    var MessageList2: List<Message>? = null
    var chatBoxAdapter: ChatBoxAdapter? = null
    var messagetxt: EditText? = null
    var send: Button? = null
    var messageVar: Message? = null
    var messageVar2: Message? = null
    private var url = GlobalParams.url + "/getmessages/"
    fun setReceiverUser(receiverUser: User?) {
        this.receiverUser1 = receiverUser
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_chat_box, container, false)
        messagetxt = view.findViewById<View>(R.id.message) as EditText
        send = view.findViewById<View>(R.id.send) as Button
        val fromLogin = activity!!.intent
        connectedUser = fromLogin.getSerializableExtra("connectedUser") as User

        //------------------
        url += connectedUser!!.id.toString() + "/" + receiverUser1!!.id
        messages
        //------------------



        //connect you socket client to the server
        try {
            //if you are using a phone device you should connect to same local network as your laptop and disable your pubic firewall as well
            socket = IO.socket(GlobalParams.url)
            //create connection
            socket!!.connect()
            // emit the event join along side with the nickname
            socket!!.emit("join", connectedUser!!.id)
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }
        MessageList = ArrayList()
        MessageList2 = ArrayList()
        myRecylerView = view.findViewById<View>(R.id.messagelist) as RecyclerView
        myRecylerView!!.adapter = chatBoxAdapter
        val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity!!.applicationContext)
        myRecylerView!!.layoutManager = mLayoutManager
        myRecylerView!!.itemAnimator = DefaultItemAnimator()
        send!!.setOnClickListener { //retrieve the nickname and the message content and fire the event //messagedetection
            if (!messagetxt!!.text.toString().isEmpty()) {
                socket!!.emit("messagedetection", connectedUser!!.id, receiverUser1!!.id, messagetxt!!.text.toString(), socket!!.id())
                messagetxt!!.setText("")
            }
            messages
        }
        socket!!.on("message") { args ->
            if (isAdded) {
                activity!!.runOnUiThread {
                    val data = args[0] as JSONObject
                    try {
                        messageVar = Message()
                        messageVar2 = Message()
                        //extract data from fired event
                        messageVar!!.content = data.getString("message")
                        messageVar!!.receiver = data.getInt("id_receiver")
                        messageVar!!.sender = data.getInt("id_sender")
                        messageVar!!.sender_name = connectedUser!!.name + ": "
                        messageVar!!.receiver_name = receiverUser1!!.name + ": "
                        messageVar!!.test = 0
                        println("My socket id: " + socket!!.id())
                        println("Other socket id: " + data.getString("socket_id"))
                        //add the message to the messageList
                        if (messageVar!!.sender == connectedUser!!.id && messageVar!!.receiver == receiverUser1!!.id || messageVar!!.sender == receiverUser1!!.id && messageVar!!.receiver == connectedUser!!.id) {
                            if (messageVar!!.sender == connectedUser!!.id && messageVar!!.receiver == receiverUser1!!.id) {
                                messageVar!!.test = 0
                                MessageList!!.add(messageVar!!)
                            } else if (messageVar!!.sender == receiverUser1!!.id && messageVar!!.receiver == connectedUser!!.id) {
                                messageVar!!.test = 1
                                MessageList!!.add(messageVar!!)
                            }
                        }
                        println("MESSAGE :: $messageVar")
                        println("MMMOOOOOPPPPP :: $MessageList")
                        // add the new updated list to the adapter
                        chatBoxAdapter = ChatBoxAdapter(MessageList!!, MessageList2!!)
                        //set the adapter for the recycler view
                        myRecylerView!!.adapter = chatBoxAdapter
                        // notify the adapter to update the recycler view
                        chatBoxAdapter!!.notifyDataSetChanged()
                        myRecylerView!!.smoothScrollToPosition(MessageList!!.size - 1)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }
        }
        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        socket!!.disconnect()
    }

    private val messages: Unit
        private get() {
            MessageList = ArrayList()
            val jsonArrayRequest = JsonObjectRequest(
                    Request.Method.GET,
                    url,
                    null,
                    Response.Listener { response ->
                        var jsonArray: JSONArray? = null
                        try {
                            jsonArray = response.getJSONArray("result")

                            for (i in 0 until jsonArray.length()) {
                                try {
                                    val jsonObject = jsonArray.getJSONObject(i)
                                    val msg = Message()
                                    msg.id = jsonObject.getInt("id")
                                    msg.sender = jsonObject.getInt("sender")
                                    msg.receiver = jsonObject.getInt("receiver")
                                    msg.content = jsonObject.getString("content")
                                    msg.sent_at = jsonObject.getString("sent_at")
                                    msg.sender_name = jsonObject.getString("sender_name")
                                    msg.receiver_name = jsonObject.getString("receiver_name")
                                    //-----------------------------------------
                                    MessageList!!.add(msg)
                                } catch (e: JSONException) {
                                    e.printStackTrace()
                                }
                            }
                            // add the new updated list to the adapter
                            chatBoxAdapter = ChatBoxAdapter(MessageList!!, MessageList2!!)
                            //set the adapter for the recycler view
                            myRecylerView!!.adapter = chatBoxAdapter
                            // notify the adapter to update the recycler view
                            chatBoxAdapter!!.notifyDataSetChanged()
                            myRecylerView!!.smoothScrollToPosition(MessageList!!.size - 1)
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    },
                    Response.ErrorListener { error -> Log.e("ERROR", error.message) }
            )
            val queue = Volley.newRequestQueue(activity!!.applicationContext)
            queue.add(jsonArrayRequest)
        }
}