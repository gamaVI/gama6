package com.example.gama6mobileapp.ui.post_a_message

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.gama6mobileapp.BuildConfig
import com.example.gama6mobileapp.databinding.FragmentPostAMessageBinding
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence


class PostAMessageFragment : Fragment() {

    private var _binding: FragmentPostAMessageBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val slideshowViewModel =
            ViewModelProvider(this).get(PostAMessageViewModel::class.java)

        _binding = FragmentPostAMessageBinding.inflate(inflater, container, false)
        val root: View = binding.root

        var messageType = ""

        val spinner: Spinner = binding.postAMessageSpinnerMessageType
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (view != null) {
                    messageType = parent.getItemAtPosition(position).toString()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                messageType = "Information"
            }
        }

        val sendButton = binding.postAMessageButtonPostMessage

        sendButton.setOnClickListener {
            val message = binding.postAMessageTextInputEditTextMessage.text.toString()
            //create toast which will display the message type and message
            binding.postAMessageTextInputEditTextMessage.text?.clear()
            sendMessage(messageType, message)
        }

        //TODO: Send message to server

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun sendMessage(messageType: String, message: String) {
        val broker = BuildConfig.BROKER
        val clientId = BuildConfig.CLIENT_ID
        val username = BuildConfig.USERNAME
        val password = BuildConfig.PASSWORD
        val topic = messageType
        val content = message

        try {
            val persistence = MemoryPersistence()
            val client = MqttClient(broker, clientId, persistence).apply {
                connect(MqttConnectOptions().apply {
                    userName = username
                    setPassword(password.toCharArray())
                })
            }

            val message = MqttMessage(content.toByteArray()).apply {
                qos = 2
            }

            client.publish(topic, message)
            Toast.makeText(context, "Message sent", Toast.LENGTH_LONG).show()
            client.disconnect()
            findNavController().navigateUp()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Message not sent", Toast.LENGTH_LONG).show()
        }
    }
}