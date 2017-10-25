package org.hello.rest

import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView


import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestTemplate

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .add(R.id.container, PlaceholderFragment())
                    .commit()
        }
    }

    override fun onStart() {
        super.onStart()
        HttpRequestTask().execute()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        if (id == R.id.action_refresh) {
            HttpRequestTask().execute()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    class PlaceholderFragment : Fragment() {

        override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
            return inflater!!.inflate(R.layout.fragment_main, container, false)
        }
    }


    private inner class HttpRequestTask : AsyncTask<Void, Void, Greeting>() {
        override fun doInBackground(vararg params: Void): Greeting? {
            try {
                val url = "http://rest-service.guides.spring.io/greeting"
                val restTemplate = RestTemplate()
                restTemplate.messageConverters.add(MappingJackson2HttpMessageConverter())
                return restTemplate.getForObject<Greeting>(url, Greeting::class.java)


            } catch (e: Exception) {
                Log.e("MainActivity", e.message, e)
            }

            return null
        }

        override fun onPostExecute(greeting: Greeting) {
            val greetingIdText = findViewById(R.id.id_value) as TextView
            val greetingContentText = findViewById(R.id.content_value) as TextView
            greetingIdText.text = greeting.id
            greetingContentText.text = greeting.content
        }

    }

}
