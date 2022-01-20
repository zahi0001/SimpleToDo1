package com.codepath.simpletodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {


    var listOfTasks = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener = object : TaskItemAdapter.OnLongClickListener {
            override fun onItemLongClicked(position: Int) {
                // 1. remove the item from the List
                listOfTasks.removeAt(position)
                // 2. Notify the adapter that our data has changed
                adapter.notifyDataSetChanged()

                saveItems()
            }
        }

        // 1. let's detect when the user clicks the add button
   //     findViewById<Button>(R.id.button).setOnClickListener {
   //         // Code in here is going to be executed when the user clicks on a button
   //         Log.i("Caren","User Clicked on button")
    //        }

        loadItems()

        //Look up the recyclerView in the layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        // Create adapter passing in the sample user data
        adapter = TaskItemAdapter(listOfTasks, onLongClickListener)
        // Attach the adapter to the recyclerview to populate items
        recyclerView.adapter = adapter
        // Set layout manager to position the items
        recyclerView.layoutManager = LinearLayoutManager(this)

        // set up the button and input field, so that the user can enter a task and add it to the list

        val inputTextField = findViewById<EditText>(R.id.addTaskField)

        //get a reference to the button
        // Set an onclick listener on to it
        findViewById<Button>(R.id.button).setOnClickListener{
            // 1.Grab teh text the user has inputted into @+id/addTaskField
            val userInputtedTask = inputTextField.text.toString()

            // 2.Add the string to out list of tasks: listOfTasks
            listOfTasks.add(userInputtedTask)

            // Notify the adaptor that our data has been updated
            adapter.notifyItemInserted(listOfTasks.size-1)

            // 3.Reset text field
            inputTextField.setText("")

            saveItems()
        }
    }

    // 1. save teh data that user has inputted
    // by writing and reading from a file

    // get the file we need
    fun getDataFile(): File {

        // every line is going to represent a specific task
        return File (filesDir, "data.txt")
    }


    // load the items by reading every line in the dat file
    fun loadItems () {
        try {
            listOfTasks = FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        } catch (ioException: IOException) {
            ioException.printStackTrace()

        }
    }

    // save the items by writing every line in the dat file
    fun saveItems () {
        try{
            FileUtils.writeLines(getDataFile(), listOfTasks)
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }

}