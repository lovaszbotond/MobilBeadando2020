package com.example.iforgotsomething.fragments.update

import android.app.AlertDialog
import android.os.Bundle
import android.os.SharedMemory
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.iforgotsomething.R
import com.example.iforgotsomething.data.models.ForgotData
import com.example.iforgotsomething.data.models.Priority
import com.example.iforgotsomething.data.viewmodel.ForgotViewModel
import com.example.iforgotsomething.databinding.FragmentUpdateBinding
import com.example.iforgotsomething.fragments.SharedViewModel
import kotlinx.android.synthetic.main.fragment_update.*
import kotlinx.android.synthetic.main.fragment_update.view.*
import kotlinx.coroutines.InternalCoroutinesApi


class UpdateFragment : Fragment() {

    //Atuomate generated by safeargs
    private val args by navArgs<UpdateFragmentArgs>()

    //Implementacio
    private val mSharedViewModel: SharedViewModel by viewModels()

    //Data binding Implementacio
    private var _binding: FragmentUpdateBinding? = null
    private val binding get() = _binding!!


    @InternalCoroutinesApi
    private val mForgotViewModel: ForgotViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //Data binding
        _binding = FragmentUpdateBinding.inflate(inflater, container, false)
        binding.args = args

        //Set menu
        setHasOptionsMenu(true)

        //View
        binding.currentFontossagiSpinner.onItemSelectedListener = mSharedViewModel.listener

        return binding.root
    }
    //Show "Hamburger"
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_fragment_menu,menu)
    }

    @InternalCoroutinesApi
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
               R.id.menu_save -> updateitem()
               R.id.menu_delete -> confirmItemRemoval()
        }

        return super.onOptionsItemSelected(item)
    }



    @InternalCoroutinesApi
    private fun updateitem() {
        val cim = current_cim_et.text.toString()
        val tartalom = current_tartalom_et.text.toString()
        val getPriority = current_fontossagi_spinner.selectedItem.toString()

        val validation = mSharedViewModel.verifyDataFromUser(cim,tartalom)
        if(validation)
        {
            val updateItem = ForgotData(
            args.currentItem.id,
            cim,
            mSharedViewModel.parsePriority(getPriority),
            tartalom
            )
            mForgotViewModel.updateData(updateItem)
            Toast.makeText(requireContext(),"Sikeres frissites!",Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }else{
            Toast.makeText(requireContext(),"Kerlek tolts ki minden mezot!",Toast.LENGTH_SHORT).show()
        }
    }

    //Logic of Delete - Show AlertDialog
    @InternalCoroutinesApi
    private fun confirmItemRemoval() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Igen") {
            _,_ -> mForgotViewModel.deleteItem(args.currentItem)
            Toast.makeText(requireContext(),"Sikeresen Eltavolitotta: ${args.currentItem.title}",Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }
        builder.setNegativeButton("Nem") { _,_ -> }
        builder.setTitle("Torles '${args.currentItem.title}'?")
        builder.setMessage("Biztosan ki szeretne torolni '${args.currentItem.title}'?")
        builder.create().show()
    }

    //Avoid memory leak
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}