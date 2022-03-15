package ru.alestrange.cultureSurgut.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import ru.alestrange.cultureSurgut.*
import ru.alestrange.cultureSurgut.data.Cultobject
import ru.alestrange.cultureSurgut.data.CycleCheckpoint
import ru.alestrange.cultureSurgut.data.CycleRoute
import ru.alestrange.cultureSurgut.databinding.ActivityCycleDetailBinding
import ru.alestrange.cultureSurgut.fragments.CycleRouteCheckpointFragment
import ru.alestrange.cultureSurgut.fragments.CycleRouteCultobjectFragment
import ru.alestrange.cultureSurgut.fragments.CycleRouteDetailFragment

private lateinit var binding: ActivityCycleDetailBinding

class CycleDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCycleDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val routeId = intent?.extras?.getInt("cycleRouteId")
        if (routeId!=null) {
            cycleRoute = SurgutCultureApplication.db.cycleRouteDao().getCycleRouteById(routeId)
            checkpoints = SurgutCultureApplication.db.cycleCheckpointDao().getCheckpointByRoute(routeId)
            cultobjects = SurgutCultureApplication.db.cultobjectCyclerouteDao().getCultobjectsByRoute(routeId)
            binding.textCycleRouteName.text= cycleRoute?.name
            val viewPager = binding.viewPager
            val pagerAdapter = ViewPagerFragmentStateAdapter(this)
            viewPager.adapter = pagerAdapter
            val tabLayout = binding.tabLayout
            tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.cycle_tab_checkpoints)))
            tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.cycle_tab_info)))
            tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.cycle_tab_cultobjects)))
            tabLayout.tabGravity = TabLayout.GRAVITY_FILL
            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    viewPager.currentItem = tab.position
                }
                override fun onTabUnselected(tab: TabLayout.Tab) {
                }
                override fun onTabReselected(tab: TabLayout.Tab) {
                }
            })
            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    val tab = tabLayout.getTabAt(position)
                    tab?.select()
                }
            })
        }
    }

    class ViewPagerFragmentStateAdapter(fm: FragmentActivity) : FragmentStateAdapter(fm) {

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> {
                    CycleRouteCheckpointFragment()
                }
                1 -> {
                    CycleRouteDetailFragment()
                }
                else -> {
                    CycleRouteCultobjectFragment()
                }
            }
        }

        override fun getItemCount(): Int {
            return 3
        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return MainMenu.menuClickHandler(this, item)
    }

    companion object{
        var cycleRoute: CycleRoute?=null
        var checkpoints: List<CycleCheckpoint>?=null
        var cultobjects: List<Cultobject>?=null
    }
}