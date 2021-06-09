package com.example.autocars.ui.base

import android.R
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.autocars.MainApplication
import com.google.android.material.snackbar.Snackbar
import es.dmoral.toasty.Toasty

abstract class BaseFragment : Fragment() {

    private val isOn = false
    var view1: View? = null
    var snackbar: Snackbar? = null
    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    @SuppressLint("ResourceType")
    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        this.view1 = view
        snackbar =
            Snackbar.make(view, "Check your internet connection.", Snackbar.LENGTH_INDEFINITE)
        val snackBarView = snackbar!!.view
  try {
      snackBarView.setBackgroundColor(
          ContextCompat.getColor(
              requireActivity(),
              resources.getColor(R.color.holo_red_dark)
          )
      )
  }catch (e:Exception){}
        val textView =
            snackBarView.findViewById<View>(com.google.android.material.R.id.snackbar_text) as TextView
        textView.gravity = View.TEXT_ALIGNMENT_CENTER
        textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        val application = requireActivity().application as MainApplication
    }

    protected fun snackBar(message: String?) {
        if (view1 != null) {
            Snackbar.make(view1!!, message!!, Snackbar.LENGTH_LONG).show()
        }
    }



    protected fun toastSuccess(msg: String?) {
        Toasty.success(MainApplication.instance!!, msg!!, Toast.LENGTH_SHORT, true).show()
    }

    protected fun toastError(msg: String?) {
        Toasty.error(MainApplication.instance!!, msg!!, Toast.LENGTH_SHORT, true).show()
    }

    protected fun toastInfo(msg: String?) {
        Toasty.info(MainApplication.instance!!, msg!!, Toast.LENGTH_SHORT, true).show()
    }

    protected fun toastWarning(msg: String?) {
        Toasty.warning(MainApplication.instance!!, msg!!, Toast.LENGTH_SHORT, true).show()
    }
}
