package com.example.iforgotsomething.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.iforgotsomething.R
import com.example.iforgotsomething.data.models.ForgotData
import com.example.iforgotsomething.data.viewmodel.ForgotViewModel
import com.example.iforgotsomething.databinding.FragmentListBinding
import com.example.iforgotsomething.fragments.SharedViewModel
import com.example.iforgotsomething.fragments.list.adatper.ListAdapter
import com.example.iforgotsomething.network.LolApi
import com.example.iforgotsomething.utils.hideKeayboard
import com.google.android.material.snackbar.Snackbar
import jp.wasabeef.recyclerview.animators.LandingAnimator
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch

class ListFragment : Fragment(), SearchView.OnQueryTextListener {

    @InternalCoroutinesApi
    private val mForgotViewModel: ForgotViewModel by viewModels()

    private val mSharedViewModel: SharedViewModel by viewModels()

    private val adapter: ListAdapter by lazy { ListAdapter() }

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private val _response = MutableLiveData<String>()
    val response: LiveData<String>
        get() = _response

    @InternalCoroutinesApi

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // With Data binding->
        _binding = FragmentListBinding.inflate(inflater,container,false)
        //Fragment - set the SharedViewModel
        binding.lifecycleOwner = this
        binding.mSharedViewModel = mSharedViewModel

        //Setup RecyclerView
        setupRecyclerView()

        //Hide KeyBoard
        hideKeayboard(requireActivity())

        //Observe the liveData
        mForgotViewModel.getAllData.observe(viewLifecycleOwner, Observer { data ->
            mSharedViewModel.checkIfDatabaseEmpty(data)
            adapter.setData(data)
        })

        //Link the Menu / Set menu
        setHasOptionsMenu(true)

        return binding.root
    }

    @InternalCoroutinesApi
    private fun setupRecyclerView() {
        val recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())

        /*  -------------------------------------------------------------------------------
        /   This is the version of 2 columns view with grid ------------------------------>
        /   -------------------------------------------------------------------------------
        /   Fix 1 issue -> StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        /   With this we can get the same sizes of box even the texts are bigger ->
        /   There wont be blank spaces
        /
        /   recyclerView.layoutManager = GridLayoutManager(requireActivity(),2)
        */

        recyclerView.itemAnimator = LandingAnimator().apply{
            addDuration = 300
        }

        //Swipe call for recylcerView
        swipeToDelete(recyclerView)

    }


    @InternalCoroutinesApi
    fun swipeToDelete(recyclerView: RecyclerView){
        val swipeToDeleteCallback = object : SwipeToDelete(){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedItem = adapter.dataList[viewHolder.adapterPosition]

                //Delete Item!
                mForgotViewModel.deleteItem(deletedItem)
                adapter.notifyItemRemoved(viewHolder.adapterPosition)

                //Restore deleted Data's
                restoreDeletedData(viewHolder.itemView,deletedItem, viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }


    @InternalCoroutinesApi
    private fun restoreDeletedData(view: View, deletedItem: ForgotData, position: Int){

        val snackBar = Snackbar.make(
                view, "Biztosan torli '${deletedItem.title}' ? ", Snackbar.LENGTH_LONG
        )
        snackBar.setAction("Visszavonas"){
            mForgotViewModel.insertData(deletedItem)

           //If we use StaggeredGridLayout -> We got an error -> in that case , we dont have to use the code line below this commit (We dont need the params 'position' anymore)
            adapter.notifyItemChanged(position)
        }
        snackBar.show()
    }


    //Show "Hamburger"
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_fragment_menu,menu)

        //Search menu item
        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)

    }

    @InternalCoroutinesApi
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId) {

            R.id.menu_delete_all -> confirmRemoval()
            R.id.menu_priority_high -> mForgotViewModel.sortByHighPriority.observe(this,Observer{adapter.setData(it)})
            R.id.menu_priority_low -> mForgotViewModel.sortByLowPriority.observe(this,Observer{adapter.setData(it)})
            R.id.menu_api -> {lolApiCall()}
        }
        return super.onOptionsItemSelected(item)
    }


     private fun lolApiCall(){
         lifecycleScope.launch {
             try {
                 val listResult = LolApi.retrofitService.getProperties()
                 _response.value = "Success: ${listResult.size} Lol Seasons Retrieved"
                 Log.i("ApiCall",_response.value!!)
             } catch (e: Exception) {
                 _response.value = "Failure: " + e.message
                 Log.i("ApiCall",_response.value!!)
             }
         }
    }


    @InternalCoroutinesApi
    override fun onQueryTextSubmit(query: String?): Boolean {
        if(query != null)
        {
            searchThroughDatabase(query)
        }
        return true
    }


    @InternalCoroutinesApi
    override fun onQueryTextChange(query: String?): Boolean {
        if(query != null)
        {
            searchThroughDatabase(query)
        }
        return true    }

    @InternalCoroutinesApi
    private fun searchThroughDatabase(query: String) {
        var searchQuery = "%$query%"


        //Observe liveData for searchDatabase
        mForgotViewModel.searchDatabase(searchQuery).observe(this, Observer {list ->
            list?.let {
               //notify recyclerView Adatpter
                adapter.setData(it)
            }
        })
    }

    // Show alertDialog to confirm delete of all items from database table
    @InternalCoroutinesApi
    private fun confirmRemoval() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Igen") {
            _,_ -> mForgotViewModel.deleteAll()
            Toast.makeText(requireContext(),"Sikeresen Eltavolitott Mindent!", Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("Nem") { _,_ -> }
        builder.setTitle("Osszes Torlese")
        builder.setMessage("Biztosan ki szeretne torolni mindent?")
        builder.create().show()
    }

    //Get away from memory leaks
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}