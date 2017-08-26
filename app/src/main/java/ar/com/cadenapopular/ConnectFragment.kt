package ar.com.cadenapopular

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.facebook.AccessToken
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.CallbackManager
import kotlinx.android.synthetic.main.fragment_connect.view.*


/**
 * Activities that contain this fragment must implement the
 * [ConnectFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ConnectFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class ConnectFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null
    private val callbackManager = CallbackManager.Factory.create()

    private var mListener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments.getString(ARG_PARAM1)
            mParam2 = arguments.getString(ARG_PARAM2)
        }
    }

    /* Facebook */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_connect, container, false)

        val accessToken = AccessToken.getCurrentAccessToken()
        if (accessToken == null) {
//            LoginManager.getInstance().logInWithPublishPermissions(this@MainActivity, listOf("publish_actions"))
            LoginManager.getInstance().logInWithPublishPermissions(this, listOf("publish_actions"))
        }
        else{
            view.connectMessage.text = getString(R.string.status_facebook_connected)
        }

        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                Log.d("MainActivity", "logged in")
                view.connectMessage.text = getString(R.string.status_facebook_connected)
            }
            override fun onCancel() {
                Log.d("MainActivity", "not logged in")
                view.connectMessage.text = "Cancel"
            }
            override fun onError(exception: FacebookException) {
                Log.d("MainActivity", "login exception")
                view.connectMessage.text = "Error"
            }
        })
        return view
    }

    override fun onResume() {
        super.onResume()
//        val facebookButton = view!!.findViewById<View>(R.id.login_button)
//        facebookButton.refreshDrawableState()
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        if (mListener != null) {
            mListener!!.onFragmentInteraction(uri)
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html) for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"
        // The request code must be 0 or greater.
        private val PLUS_ONE_REQUEST_CODE = 0

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.

         * @param param1 Parameter 1.
         * *
         * @param param2 Parameter 2.
         * *
         * @return A new instance of fragment ConnectFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): ConnectFragment {
            val fragment = ConnectFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }

}// Required empty public constructor
