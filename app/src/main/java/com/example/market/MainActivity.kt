package com.example.market

import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.market.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)

        val itemList = Item.getAllItems()
        val adapter = ItemAdapter(itemList) { item ->
            showItemDetail(item)
        }

        recyclerView.adapter = adapter

        binding.btnSelect.setOnClickListener {
            recyclerView.smoothScrollToPosition(adapter.itemCount - 1)
        }

        binding.btnFloating.setOnClickListener {
            recyclerView.smoothScrollToPosition(0)
        }

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            private var lastScrollPosition = 0
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val currentScrollPosition = recyclerView.computeVerticalScrollOffset()
                if (currentScrollPosition > lastScrollPosition && binding.btnFloating.alpha == 0f) {
                    binding.btnFloating.animate().alpha(1f).setDuration(300).start()
                } else if (currentScrollPosition < lastScrollPosition && binding.btnFloating.alpha == 1f) {
                    binding.btnFloating.animate().alpha(0f).setDuration(300).start()
                }
                lastScrollPosition = currentScrollPosition
            }
        })

        binding.ivNotify.setOnClickListener {
            notification()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        showExitDialog()
    }

    private fun showItemDetail(item: Item) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("item", item)
        startActivity(intent)
    }

    private fun notification() {
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "one-channel"
        val channelName = "My Channel One"
        val notificationId = 1

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "My Channel One Description"
                enableVibration(true)
            }
            manager.createNotificationChannel(channel)
        }

        val intent = Intent(this, DetailActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            notificationId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("새 알림이 있습니다.")
            .setContentText("알림을 누르세요.")
            .setSmallIcon(R.drawable.notify)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        manager.notify(notificationId, notification)
    }

    private fun showExitDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("종료하시겠습니까?")
        builder.setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int ->
            finish()
        }
        builder.setNegativeButton("취소") { dialogInterface: DialogInterface, i: Int ->
            dialogInterface.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }
}