package br.edu.ufabc.ufabcbarganha.feed.general

import android.content.Intent
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import android.view.MenuItem
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import br.edu.ufabc.ufabcbarganha.*
import br.edu.ufabc.ufabcbarganha.model.Post
import br.edu.ufabc.ufabcbarganha.user.MyInterestsActivity
import br.edu.ufabc.ufabcbarganha.user.MyPostsActivity
import com.google.android.material.tabs.TabLayout

class FeedActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var currentType = Post.PostType.PRODUCT
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)

        val toolbar: Toolbar = findViewById(R.id.toolbar)

        val viewPager = findViewById<ViewPager>(R.id.view_pager)
        viewPager.adapter = MyPagerAdapter(supportFragmentManager)
        viewPager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {
                //(y)
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                updateType(position)
            }

            override fun onPageSelected(position: Int) {
                updateType(position)
            }

            private fun updateType(pos: Int) {
                currentType = Post.PostType.values()[pos]
            }

        })


        val fab: FloatingActionButton = findViewById(R.id.create_post_fab)
        fab.setOnClickListener {
            val intent = Intent(this, CreatePostActivity::class.java)

            intent.putExtra(App.POST_TYPE_EXTRA, currentType)
            startActivity(intent)
        }

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)

        val tabLayout = findViewById<TabLayout>(R.id.tab_layout)
        tabLayout.setupWithViewPager(viewPager)

    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_my_posts -> {
                startActivity(Intent(this, MyPostsActivity::class.java))
            }
            R.id.nav_my_interests -> {
                startActivity(Intent(this, MyInterestsActivity::class.java))
            }
            R.id.nav_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
            }
            R.id.nav_logout -> {
                // TODO: clear user info

                finish()
            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
