package com.health.openworkout.gui.datatypes

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.Toast

import androidx.annotation.Keep
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.Navigation.findNavController

import com.health.openworkout.R

abstract class GenericSettingsFragment : Fragment(), MenuProvider {
    @Keep
    enum class SETTING_MODE {
        EDIT,
        ADD
    }

    protected var mode = SETTING_MODE.EDIT
        set(value) {
            field = value
            loadFromDatabase(value)
        }

    protected abstract fun getTitle(): String?
    protected abstract fun loadFromDatabase(mode: SETTING_MODE)
    protected abstract fun saveToDatabase(mode: SETTING_MODE): Boolean

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.fragment_menu, menu)
        val editMenu = menu.findItem(R.id.edit)
        editMenu.isVisible = false
    }

    override fun onMenuItemSelected(item: MenuItem): Boolean {
        // Close keyboard
        if (requireActivity().currentFocus != null) {
            val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(requireActivity().currentFocus!!.windowToken, 0)
        }

        return when (item.itemId) {
            R.id.save -> {
                if (saveToDatabase(mode)) {
                    Toast.makeText(context, String.format(getString(R.string.label_save_toast), getTitle()), Toast.LENGTH_SHORT).show()
                    findNavController(requireActivity(), R.id.nav_host_fragment).navigateUp()
                }
                true
            }
            R.id.reset -> {
                Toast.makeText(context, String.format(getString(R.string.label_reset_toast), getTitle()), Toast.LENGTH_SHORT).show()
                loadFromDatabase(mode)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
