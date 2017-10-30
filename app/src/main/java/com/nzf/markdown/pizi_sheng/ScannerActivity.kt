package com.nzf.markdown.pizi_sheng

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.hardware.camera2.*
import android.media.Image
import android.media.ImageReader
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.support.v7.app.AppCompatActivity
import android.util.SparseIntArray
import android.view.Surface
import android.view.SurfaceHolder
import android.view.View
import android.widget.Toast
import com.nzf.markdown.R
import kotlinx.android.synthetic.main.activity_scanner.*
import java.nio.ByteBuffer
import java.util.*

/**
 * Created by joseph on 2017/10/30.
 */
class ScannerActivity : AppCompatActivity(), View.OnClickListener{

    init {
        ORIENTATIONS.append(Surface.ROTATION_0,90)
        ORIENTATIONS.append(Surface.ROTATION_90,0)
        ORIENTATIONS.append(Surface.ROTATION_180,270)
        ORIENTATIONS.append(Surface.ROTATION_270,180)
    }


    companion object {
        val ORIENTATIONS : SparseIntArray = SparseIntArray()
    }

    private var mSurfaceHolder : SurfaceHolder? = null
    private var mCameraManager : CameraManager? = null
    private var childHandler : Handler? = null
    private var mainHandler : Handler? = null
    private var mCameraID : String? = null
    private var mImageReader : ImageReader? = null
    private var mCameraCaptureSession : CameraCaptureSession? = null
    private var mCameraDevice : CameraDevice? = null

    override fun onClick(v: View?) {
        takePicture()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanner)
        initView()
    }


    private fun initView(){
        surface!!.setOnClickListener(this@ScannerActivity)
        mSurfaceHolder = surface!!.holder
        mSurfaceHolder!!.setKeepScreenOn(true)

        mSurfaceHolder!!.addCallback(object : SurfaceHolder.Callback{
            override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {

            }

            override fun surfaceDestroyed(holder: SurfaceHolder?) {
                mCameraDevice!!.close()
                mCameraDevice = null
            }

            override fun surfaceCreated(holder: SurfaceHolder?) {
                initCamera2()
            }


        })


    }


    @SuppressLint("MissingPermission")
    private fun initCamera2() {
        val handlerThread = HandlerThread("Camera2")
        handlerThread.start()
        childHandler = Handler(handlerThread.looper)
        mainHandler = Handler(mainLooper)

        mCameraID = "${CameraCharacteristics.LENS_FACING_FRONT}"
        mImageReader = ImageReader.newInstance(1080,1920, ImageFormat.JPEG,1)

        mImageReader!!.setOnImageAvailableListener({ reader ->
            mCameraDevice!!.close()
            surface!!.visibility = View.GONE

            iv_show!!.visibility = View.VISIBLE
            val image : Image = reader!!.acquireNextImage()
            val buffer : ByteBuffer = image.planes[0].buffer
            val bytes : ByteArray = kotlin.ByteArray(buffer.remaining())
            buffer.get(bytes)
            val bitmap : Bitmap =  BitmapFactory.decodeByteArray(bytes,0,bytes.size)
            iv_show!!.setImageBitmap(bitmap)
        },mainHandler)

        mCameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager?
        try {
            mCameraManager!!.openCamera(mCameraID,stateCallback,mainHandler)
        }catch (e : Exception){
            e.printStackTrace()
        }

    }

    private val stateCallback : CameraDevice.StateCallback = object : CameraDevice.StateCallback(){
        override fun onOpened(camera: CameraDevice?) {
            mCameraDevice = camera
            takePreview()
        }

        override fun onDisconnected(camera: CameraDevice?) {
            mCameraDevice!!.close()
            mCameraDevice = null
        }

        override fun onError(camera: CameraDevice?, error: Int) {
            Toast.makeText(this@ScannerActivity,"摄像头开启失败", Toast.LENGTH_SHORT).show()
        }
    }


    /**
     * 开始预览
     */
    private fun takePreview() {
        try{
            //创建预览需要的CaptureRequest.Builder
            val previewRequestBuilder : CaptureRequest.Builder? = mCameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
            //将surfaceView的 surface 作为CaptureRequest.Builder的目标
            previewRequestBuilder!!.addTarget(mSurfaceHolder!!.surface)
            //创建CameraCaptureSession,该对象负责管理处理预览请求和拍照请求
            mCameraDevice!!.createCaptureSession(Arrays.asList(mSurfaceHolder!!.surface,mImageReader!!.surface),
                    object : CameraCaptureSession.StateCallback(){
                        override fun onConfigured(session: CameraCaptureSession?) {
                            mCameraCaptureSession = session
                            try{
                                //自动对焦
                                previewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE,
                                        CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE)
                                //打开闪光灯
                                previewRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE,
                                        CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH)
                                //显示预览
                                val previewRequest : CaptureRequest = previewRequestBuilder.build()
                                mCameraCaptureSession!!.setRepeatingRequest(previewRequest,null,childHandler)

                            }catch (e : CameraAccessException){
                            }
                        }

                        override fun onConfigureFailed(session: CameraCaptureSession?) {
                            Toast.makeText(this@ScannerActivity,"配置失败", Toast.LENGTH_SHORT).show()
                        }

                    },childHandler)

        }catch (e : Exception){}
    }



    private fun takePicture() {
        //创建拍照需要的CaptureRequest.Builder

        val captureRequestBuilder : CaptureRequest.Builder
        try{
            captureRequestBuilder = mCameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
            // 将imageReader的surface作为CaptureRequest.Builder的目标
            captureRequestBuilder.addTarget(mImageReader!!.surface)
            // 自动对焦
            captureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE)
            // 自动曝光
            captureRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH)
            // 获取手机方向
            val rotation : Int = windowManager.defaultDisplay.rotation

            // 根据设备方向计算设置照片的方向
            captureRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation))
            //拍照
            val mCaptureRequest : CaptureRequest? = captureRequestBuilder.build()
            mCameraCaptureSession!!.capture(mCaptureRequest, null, childHandler)

        } catch (e : Exception){

        }

    }

}