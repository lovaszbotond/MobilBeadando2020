package com.example.iforgotsomething.fragments.add

import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.iforgotsomething.R
import com.example.iforgotsomething.data.models.ForgotData
import com.example.iforgotsomething.data.models.Priority
import com.example.iforgotsomething.data.viewmodel.ForgotViewModel
import com.example.iforgotsomething.fragments.SharedViewModel
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.android.synthetic.main.fragment_add.view.*
import kotlinx.coroutines.InternalCoroutinesApi


class AddFragment : Fragment() {

    //Delegate to viewModel ( On option item selected override )
    @InternalCoroutinesApi
    private val mForgotViewModel: ForgotViewModel by viewModels()

    private val mSharedViewModel: SharedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment - here we set up , that we want to insert data into our database
        val view = inflater.inflate(R.layout.fragment_add, container, false)

        //Set options menu for add
        setHasOptionsMenu(true)

        view.fontossagi_spinner.onItemSelectedListener = mSharedViewModel.listener

        return view

    }

    //Show "Hamburger"
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_fragment_menu,menu)
    }

    //Handle onClickListeners for our menu items
    @InternalCoroutinesApi
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == R.id.menu_add)
            {
                insertDataToDb()
            }
        return super.onOptionsItemSelected(item)
    }

    //Here is the logic of inserting the Data
    @InternalCoroutinesApi
    private fun insertDataToDb() {
        val mCim = cim_et.text.toString()
        val mSorrend = fontossagi_spinner.selectedItem.toString()
        val mTartalomBox = tartalom_et.text.toString()

        val validation = mSharedViewModel.verifyDataFromUser(mCim,mTartalomBox)
        if(validation){
            //Insert Data to Database !!!!
            val newData = ForgotData(
                    0,
                    mCim,
                    mSharedViewModel.parsePriority(mSorrend),
                    mTartalomBox
            )
            mForgotViewModel.insertData(newData)
            Toast.makeText(requireContext(), "Sikeresen Hozzaadva", Toast.LENGTH_SHORT).show()
            //Navigate Back to Our listFragment
            findNavController().navigate(AddFragmentDirections.actionAddFragmentToListFragment())
        }else {
            Toast.makeText(requireContext(), "Kerlek tolts ki minden mezot", Toast.LENGTH_SHORT).show()
        }
    }

}