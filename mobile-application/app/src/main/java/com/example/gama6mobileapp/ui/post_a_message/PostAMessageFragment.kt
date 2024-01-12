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
import com.example.gama6mobileapp.databinding.FragmentPostAMessageBinding


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
            Toast.makeText(
                context,
                "Message type: $messageType\nMessage: $message",
                Toast.LENGTH_LONG
            ).show()
        }

        //TODO: Send message to server

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}