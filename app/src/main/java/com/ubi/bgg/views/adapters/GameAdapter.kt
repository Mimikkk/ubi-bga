package com.ubi.bgg.views.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.ubi.bgg.Common
import com.ubi.bgg.R
import com.ubi.bgg.database.entities.Game
import com.ubi.bgg.databinding.LiGameBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.net.URL

class GameAdapter(private val context: Activity, private val list: List<Game>) :
  ArrayAdapter<Game>(context, R.layout.li_game, list) {
  private lateinit var binding: LiGameBinding

  @SuppressLint("ViewHolder", "SetTextI18n")
  override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
    binding = LiGameBinding.inflate(LayoutInflater.from(context), parent, false)


    list[position].thumbnail?.let { thumbnail(it) }
    binding.tvOrderNo.text = (position + 1).toString()
    binding.tvTitle.text = list[position].title
    binding.tvYear.text = year(list[position].published)

    val ranks = Common.Database.games().ranks(list[position].id)
    val rank = ranks.firstOrNull()
    binding.tvRank.text = rank(rank?.position)
    binding.tvScore.text = score(rank?.score)

    return binding.root
  }

  private fun thumbnail(url: String) {

    runBlocking {
      withContext(Dispatchers.IO) {
        val bitmap = BitmapFactory.decodeStream(URL(url).openConnection().getInputStream())
        binding.ivThumbnail.setImageBitmap(bitmap)
      }
    }
  }

  private fun year(year: Int?) = year?.toString() ?: "N/D"
  private fun rank(rank: Int?) = "ranking - ${rank?.toString() ?: "nie oceniony"}"

  private fun score(score: Double?) = score?.let { "%.2f".format(score) } ?: "-"
}
