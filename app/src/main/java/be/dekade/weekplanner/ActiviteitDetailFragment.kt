package be.dekade.weekplanner

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import be.dekade.weekplanner.data.ActiviteitEnDagGegevensRepository
import be.dekade.weekplanner.databinding.FragmentDetailActiviteitBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ActiviteitDetailFragment : Fragment() {

    private val SPEECH_REQUEST_TITEL_CODE = 0
    private val SPEECH_REQUEST_NOTITIES_CODE = 1

    private val viewModel : ActiviteitDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentDetailActiviteitBinding.inflate(inflater, container, false)
        context?: return binding.root
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        viewModel.activiteit.observe(viewLifecycleOwner, {a ->
            if (a != null) {
                    viewModel.activiteitChanged()
                }
        })
        viewModel.eventActiviteitSubmitted.observe(viewLifecycleOwner, { isSubmitted ->
            if(isSubmitted){
                viewModel.onSubmitComplete()
                findNavController().popBackStack()
            }
        })
        viewModel.foutmelding.observe(viewLifecycleOwner,{ foutmelding ->
            if(!foutmelding.isNullOrEmpty()){
                viewModel.onFoutmeldingHandled()
                Toast.makeText(activity,foutmelding, Toast.LENGTH_LONG).show()
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
        viewModel.eventActiviteitDeleted.observe(viewLifecycleOwner, {isDeleted ->
            if(isDeleted){
                viewModel.onDeleteComplete()
                findNavController().popBackStack()
            }
        })

        setHasOptionsMenu(true)
        return binding.root
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