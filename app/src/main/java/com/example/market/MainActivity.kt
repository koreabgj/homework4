package com.example.market

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.market.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val exitDialog: AlertDialog by lazy {
        createExitDialog()
    }
    private lateinit var notificationManager: NotificationManager
    private val channelName = "My Channel One"
    private val channelId = "one-channel"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRecyclerView()
        setButtonClickListeners()
        checkNotificationPermission()
        setupNotificationChannel()
    }

    private fun initRecyclerView() {
        val adapter = ItemAdapter(::showItemDetail)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
        adapter.submitList(itemList)
    }

    private fun setButtonClickListeners() {
        binding.btnEnd.setOnClickListener {
            showExitDialog()
        }

        binding.btnSelect.setOnClickListener {
            binding.recyclerView.smoothScrollToPosition(binding.recyclerView.adapter!!.itemCount - 1)
        }

        binding.btnFloating.setOnClickListener {
            binding.recyclerView.smoothScrollToPosition(0)
        }

        binding.ivNotify.setOnClickListener {
            showNotification()
        }
    }

    private fun createExitDialog() =
        AlertDialog.Builder(this)
            .setMessage("종료하시겠습니까?")
            .setPositiveButton("확인") { _: DialogInterface, _: Int ->
                finish()
            }
            .setNegativeButton("취소") { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
            }
            .create()

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1);
            }
        }
    }

    private fun setupNotificationChannel() {
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "My Channel One Description"
                enableVibration(true)
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    @SuppressLint("MissingSuperCall")
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        showExitDialog()
        //super.onBackPressed()
    }

    private fun showItemDetail(item: Item) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("item", item)
        startActivity(intent)
    }

    private fun showNotification() {
        val notificationId = 1
        val intent = Intent(this, DetailActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            notificationId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("새 알림이 있습니다.")
            .setContentText("알림을 누르세요.")
            .setSmallIcon(R.drawable.notify)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        if (::notificationManager.isInitialized)
            notificationManager.notify(notificationId, notification)
    }

    private fun showExitDialog() {
        exitDialog.show()
    }
}