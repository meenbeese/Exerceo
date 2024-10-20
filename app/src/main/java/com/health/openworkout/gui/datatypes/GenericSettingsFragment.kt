package com.health.openworkout.gui.datatypes

import android.content.Context
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.Toast

import androidx.annotation.Keep
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController

import com.health.openworkout.R

abstract class GenericSettingsFragment : Fragment() {
    @Keep
    enum class SETTING_MODE {
        EDIT,
        ADD
    }

    private var mode = SETTING_MODE.EDIT

    init {
        setHasOptionsMenu(true)
    }

    protected abstract val title: String?

    protected abstract fun loadFromDatabase(mode: SETTING_MODE?)
    protected abstract fun saveToDatabase(mode: SETTING_MODE?): Boolean

    protected fun setMode(mode: SETTING_MODE) {
        this.mode = mode
        loadFromDatabase(mode)
    }

    protected fun getMode(): SETTING_MODE {
        return mode
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragment_menu, menu)

        val editMenu = menu.findItem(R.id.edit)
        editMenu.setVisible(false)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Close keyboard
        if (requireActivity().currentFocus != null) {
            val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(requireActivity().currentFocus!!.windowToken, 0)
        }

        when (item.itemId) {
            R.id.save -> {
                if (saveToDatabase(mode)) {
                    Toast.makeText(context, String.format(getString(R.string.label_save_toast), title), Toast.LENGTH_SHORT).show()
                    findNavController(requireActivity(), R.id.nav_host_fragment).navigateUp()
                }
                return true
            }

            R.id.reset -> {
                Toast.makeText(context, String.format(getString(R.string.label_reset_toast), title), Toast.LENGTH_SHORT).show()
                loadFromDatabase(mode)
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }
}
