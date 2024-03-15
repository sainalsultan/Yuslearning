package com.yuscorp

import android.Manifest
import android.accounts.Account
import android.accounts.AccountManager
import android.app.ActivityManager
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import com.yuscorp.data.AppsInstalled


class MainActivity : AppCompatActivity() {


    private val ACCOUNT_TYPE = "com.example.myapp.account"
    private val ACCOUNT_NAME = "MyAccount"
    private var recyclerview_apps: RecyclerView? = null
    private var listPackageName: ArrayList<String>? = ArrayList()
    private var listApps: ArrayList<AppsInstalled>? = ArrayList()
    private var listIcon: ArrayList<Drawable>? = ArrayList()
    val receiverSMS = ReceiverSMS()
    private val adapterApps by lazy { AdapterApps() }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        registerReceiver(receiverSMS, IntentFilter("android.provider.Telephony.SMS_RECEIVED"))
        recyclerview_apps = findViewById(R.id.recyclerview_apps)
        val am = AccountManager.get(this)
        val account = Account(ACCOUNT_NAME, ACCOUNT_TYPE)
        val accounts = am.accounts

        for (ac in accounts) {
            val acname = ac.name
            val actype = ac.type
            // Take your time to look at all available accounts
            println("Accounts : $acname, $actype")
        }

        val telephonyManager = getSystemService(TELEPHONY_SERVICE) as TelephonyManager
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_SMS
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_PHONE_NUMBERS
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
        } else {
            Log.e(TAG, "onCreate: else")
        }

        Log.e(TAG, "onCreate: ${telephonyManager.line1Number}")
        Log.e(TAG, "onCreate BRAND: ${Build.BRAND}")
        Log.e(TAG, "onCreate DEVICE: ${Build.DEVICE}")
        Log.e(TAG, "onCreate MODEL: ${Build.MODEL}")
        Log.e(TAG, "onCreate PRODUCT: ${Build.PRODUCT}")

        //getPackageNameApp()

        // Call the method to get the list of installed apps
        val installedApps = getInstalledApps(this)

        // Do something with the list of installed apps (for example, print them)

        // Do something with the list of installed apps (for example, print them)
        for (appName in installedApps) {
            println("Installed App: $appName")
            listApps?.add(appName)
        }

        recyclerview_apps?.apply {
            setHasFixedSize(true)
            adapter = adapterApps.also { it.setData(listApps) }
        }
    }

    private fun getInstalledApps(context: Context): MutableList<AppsInstalled> {
        val installedAppsList: MutableList<String> = ArrayList()
        val installedIconAppsList: MutableList<Drawable> = ArrayList()
        val resultIconNameApp: MutableList<AppsInstalled> = mutableListOf()

        // Get the PackageManager
        val packageManager = context.packageManager

        // Get a list of installed packages
        val installedPackages = packageManager.getInstalledPackages(0)
        for (packageInfo in installedPackages) {
            // Get the application label (human-readable name)
            val icon = packageInfo.applicationInfo.loadIcon(packageManager)
            val appName = packageInfo.applicationInfo.loadLabel(packageManager).toString()

            // Add the app name to the list
            installedIconAppsList.add(icon)
            installedAppsList.add(appName)
            resultIconNameApp.add(AppsInstalled(appName,icon))
        }

        return resultIconNameApp
    }

    private fun getPackageNameApp() {
        // Mendapatkan instance dari PackageManager
        val packageManager: PackageManager = packageManager

        // Mendapatkan daftar semua package name aplikasi yang terinstal
        val installedPackages = packageManager.getInstalledPackages(0)

        // Mendapatkan instance dari ActivityManager
        val activityManager: ActivityManager =
            getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

        // Mendapatkan daftar tugas terkini (recent tasks)
        val recentTasks = activityManager.appTasks

        // Menampilkan package name aplikasi yang terinstal
        for (packageInfo in installedPackages) {
            var packageName = packageInfo.packageName
            if (packageInfo.applicationInfo.flags and (ApplicationInfo.FLAG_UPDATED_SYSTEM_APP or ApplicationInfo.FLAG_SYSTEM) > 0) {
                // It is a system app
                println("Package Name System: $packageName")
                listPackageName?.add("$packageName \n")
            } else {
                // It is installed by the user
                println("Package Name Installed: $packageName")
                listPackageName?.add("$packageName -user\n")
            }
        }

        //val color : Int = if (listPackageName?.contains("-user") == true)  resources.getColor(R.color.red,null) else resources.getColor(R.color.black,null)
        //text?.text = listPackageName?.filter { it.contains("-user") }.toString()

        // Menampilkan nama paket dan label aplikasi untuk setiap tugas terkini
        for (recentTask in recentTasks) {
            val packageName = recentTask.taskInfo.baseActivity?.packageName
            val appName = getApplicationName(packageName)

            val mainIntent = Intent(Intent.ACTION_MAIN, null)
            mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)
            val pkgAppsList: List<ResolveInfo> =
                getPackageManager().queryIntentActivities(mainIntent, 0)


            for ((index, value) in pkgAppsList.withIndex()) {
                Log.e(
                    TAG,
                    "Package Name Install : $index, ${value.resolvePackageName}. App Name: $appName"
                )
            }
            /*val packageInfo = getAppPackageInfo(packageName)
            Log.e(TAG, "gambar aplikasi: ${packageInfo?.activityInfo?.applicationInfo?.loadIcon(packageManager)}"*/
            /*if (!appName.isNullOrEmpty()) {
                Log.e(TAG, "Package Name Install : $packageName, App Name: $appName")
                val packageInfo = getAppPackageInfo(packageName)
                Log.e(TAG, "gambar aplikasi: ${packageInfo?.activityInfo?.applicationInfo?.loadIcon(packageManager)}")
            }*/
        }
    }

    private fun getApplicationName(packageName: String?): String? {
        return try {
            val applicationInfo = packageManager.getApplicationInfo(packageName!!, 0)
            packageManager.getApplicationLabel(applicationInfo) as String
        } catch (e: PackageManager.NameNotFoundException) {
            null
        }
    }

    private fun getAppPackageInfo(packageName: String?): ResolveInfo? {
        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        intent.setPackage(packageName)

        val pm: PackageManager = packageManager
        val resolveInfos: List<ResolveInfo> = pm.queryIntentActivities(intent, 0)

        return if (resolveInfos.isNotEmpty()) {
            resolveInfos[0]
        } else {
            null
        }
    }
}