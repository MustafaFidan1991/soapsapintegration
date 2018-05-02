package com.mustafafidan.soapexample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import org.ksoap2.SoapEnvelope
import org.ksoap2.serialization.SoapObject
import org.ksoap2.serialization.SoapSerializationEnvelope
import org.ksoap2.transport.HttpTransportSE
import android.os.AsyncTask
import android.util.Log
import android.os.StrictMode
import org.ksoap2.transport.ServiceConnection
import org.ksoap2.transport.ServiceConnectionSE
import java.io.IOException


class MainActivity : AppCompatActivity() {
    var NAMESPACE = "urn:sap-com:document:sap:rfc:functions"
    var METHOD_NAME = "ZDENEME_FNK"
    var SOAP_ACTION = "urn:sap-com:document:sap:rfc:functions:ZDENEME_FNK_SRV:ZDENEME_FNKRequest"
    var URL = "http://BZ-SRV-SAPTST.bize.local:8001/sap/bc/srt/rfc/sap/zdeneme_fnk_srv/800/zdeneme_fnk_srv/zdeneme_fnk_srv"


    private val USERNAME = "testuser"

    private val PASSWORD = "123123123"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)




        SoapOperation().execute("")

    }


    private inner class SoapOperation : AsyncTask<String, Void, String>() {

        override fun doInBackground(vararg params: String): String {

            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()

            StrictMode.setThreadPolicy(policy)

            val request = SoapObject(NAMESPACE, METHOD_NAME)


            request.addProperty("I_POSNR", "00010")
            request.addProperty("I_VBELN", "0000000043")
            request.addProperty("I_WERKS", "2400")

            val envelope = SoapSerializationEnvelope(SoapEnvelope.VER11)

            envelope.setOutputSoapObject(request)

            var androidHttpTransport: AuthTransportSE? = null

            try {

                androidHttpTransport = AuthTransportSE(URL, USERNAME, PASSWORD)

                androidHttpTransport!!.debug = true

            } catch (ex: Exception) {

                println("eror: $ex")

            }


            try {

                androidHttpTransport!!.call(SOAP_ACTION, envelope)

                val response = envelope.bodyIn as SoapObject
                /********* Process output here  */
                Log.d("","")

            }
            catch (e: Exception) {

                e.printStackTrace()

            }


            return "Executed"
        }

        override fun onPostExecute(result: String) {

        }

        override fun onPreExecute() {}

        override fun onProgressUpdate(vararg values: Void) {}
    }


    inner class AuthTransportSE(url: String, private val username: String?, private val password: String?) : HttpTransportSE(url) {

        @Throws(IOException::class)
        public override fun getServiceConnection(): ServiceConnection {
            val midpConnection = ServiceConnectionSE(url)
            addBasicAuthentication(midpConnection)
            return midpConnection
        }

        @Throws(IOException::class)
        protected fun addBasicAuthentication(midpConnection: ServiceConnection) {
            if (username != null && password != null) {
                val buf = StringBuffer(username)
                buf.append(':').append(password)
                val raw = buf.toString().toByteArray()
                buf.setLength(0)
                buf.append("Basic ")
                org.kobjects.base64.Base64.encode(raw, 0, raw.size, buf)
                midpConnection.setRequestProperty("Authorization", buf.toString())
            }
        }
    }

}
