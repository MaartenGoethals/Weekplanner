package be.dekade.weekplanner

import android.app.*
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import be.dekade.weekplanner.databinding.FragmentNieuweActiviteitBinding
import be.dekade.weekplanner.helpers.CHANNELID
import be.dekade.weekplanner.helpers.NOTIFICATIONID
import be.dekade.weekplanner.helpers.messageExtra
import be.dekade.weekplanner.helpers.titleExtra
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class NieuweActiviteitFragment : Fragment() {
    private val SPEECH_REQUEST_TITEL_CODE = 0
    private val SPEECH_REQUEST_NOTITIES_CODE = 1

    private val viewModel: NieuweActiviteitViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentNieuweActiviteitBinding.inflate(inflater, container, false)
        context?: return binding.root

        binding.viewModel=viewModel

        viewModel.eventActiviteitSubmitted.observe(viewLifecycleOwner, { isSubmitted ->
            if(isSubmitted){
                viewModel.onSubmitComplete()
                findNavController().popBackStack()
            }
        })
        viewModel.foutmelding.observe(viewLifecycleOwner,{ foutmelding ->
            if(!foutmelding.isNullOrEmpty()){

                scheduleNotification()
                viewModel.onFoutmeldingHandled()
                Toast.makeText(context,foutmelding, Toast.LENGTH_LONG).show()
            }
        })
        viewModel.eventTitelVoiceInput.observe(viewLifecycleOwner,{isGestart ->
            if(isGestart){
                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                    putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                }
                // This starts the activity and populates the intent with the speech text.
                startActivityForResult(intent, SPEECH_REQUEST_TITEL_CODE)
                viewModel.onVoiceInputComplete()
            }
        })
        viewModel.evenNotitiesVoiceInput.observe(viewLifecycleOwner,{isGestart ->
            if(isGestart){
                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                    putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                }
                // This starts the activity and populates the intent with the speech text.
                startActivityForResult(intent, SPEECH_REQUEST_NOTITIES_CODE)
                viewModel.onVoiceInputComplete()
            }
        })
        setHasOptionsMenu(true)
        return binding.root
    }

    private fun scheduleNotification() {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == SPEECH_REQUEST_TITEL_CODE && resultCode == Activity.RESULT_OK) {
            val spokenText: String? =
                data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).let { results ->
                    results?.get(0)
                }
            if (spokenText != null) {
                (view?.findViewById(R.id.titel_text) as EditText).setText(spokenText)
            }
        }else if(requestCode == SPEECH_REQUEST_NOTITIES_CODE && resultCode == Activity.RESULT_OK){
            val spokenText: String? =
                data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).let { results ->
                    results?.get(0)
                }
            if (spokenText != null) {
                (view?.findViewById(R.id.notities_text) as EditText).setText(spokenText)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}