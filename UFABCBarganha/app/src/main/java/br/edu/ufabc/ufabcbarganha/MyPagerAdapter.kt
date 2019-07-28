package br.edu.ufabc.ufabcbarganha

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import br.edu.ufabc.ufabcbarganha.feed.food.FoodFragment
import br.edu.ufabc.ufabcbarganha.feed.housing.HousingFragment
import br.edu.ufabc.ufabcbarganha.feed.products.ProductFragment

class MyPagerAdapter (fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> ProductFragment()
            1 -> HousingFragment()
            else -> FoodFragment()
        }
    }

    override fun getCount(): Int {
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence? {
        val pageTitle = when (position) {
            0 -> R.string.product_title
            1 -> R.string.housing_title
            else -> R.string.food_title
        }

        return App.context.getResources().getString(pageTitle)
    }
}