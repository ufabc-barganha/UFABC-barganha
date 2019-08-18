package br.edu.ufabc.ufabcbarganha.feed.general

import android.content.Intent
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import android.view.MenuItem
import android.widget.TextView
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import br.edu.ufabc.ufabcbarganha.*
import br.edu.ufabc.ufabcbarganha.login.LoginActivity
import br.edu.ufabc.ufabcbarganha.user.MyInterestsActivity
import br.edu.ufabc.ufabcbarganha.user.MyPostsActivity
import br.edu.ufabc.ufabcbarganha.user.data.FirebaseUserHelper
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth

class FeedActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)

        val toolbar: Toolbar = findViewById(R.id.toolbar)

        val fab: FloatingActionButton = findViewById(R.id.create_post_fab)
        fab.setOnClickListener { view ->
            startActivity(Intent(this, CreatePostActivity::class.java))
        }


        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navHeader = navView.getHeaderView(0)

        navHeader.findViewById<TextView>(R.id.tv_navigation_user_name).text = FirebaseUserHelper.getUserName()
        navHeader.findViewById<TextView>(R.id.tv_navigation_user_email).text = FirebaseUserHelper.getUserEmail()

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)

        val viewPager = findViewById<ViewPager>(R.id.view_pager)
        viewPager.adapter = MyPagerAdapter(supportFragmentManager)

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
                mAuth.signOut()

                Intent(this, LoginActivity::class.java).apply {
                    putExtra(App.IS_RETURN, true)
                    startActivity(this)
                }

                finish()
            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
