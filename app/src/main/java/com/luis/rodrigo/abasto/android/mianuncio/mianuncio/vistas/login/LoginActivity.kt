package com.luis.rodrigo.abasto.android.mianuncio.mianuncio.vistas.login

import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.SignInButton
import com.google.firebase.auth.FirebaseAuth
import com.luis.rodrigo.abasto.android.mianuncio.mianuncio.R
import com.luis.rodrigo.abasto.android.mianuncio.mianuncio.modelos.UserViewModel
import com.luis.rodrigo.abasto.android.mianuncio.mianuncio.utils.L
import com.luis.rodrigo.abasto.android.mianuncio.mianuncio.vistas.BaseActivity


class LoginActivity : BaseActivity(), View.OnClickListener {

    private val mCodigoSingIn=9001
    private var mAuth: FirebaseAuth? = null
    private var mGoogleSignInClient: GoogleSignInClient? = null
    private var mProgressBar: ProgressBar? = null
    private var mStatusTextView: TextView? = null
    private var mDetailTextView: TextView? = null
    private var mButtonSingIn: SignInButton? = null
    private var mButtonSingOut: Button? = null
    private var mButtonDisconnect: Button? = null
    private var mDisconnectContainer: LinearLayout? = null

    private var mContadorTextView: TextView? = null
    private var mCambioInformacionTextView: TextView? = null
    private var mCambioInformacionButton: Button? = null

    private lateinit var mUserViewModel: UserViewModel
    private var contador=0


    override fun onClick(v: View?) {
        when (v?.id) {
            mButtonSingIn?.id -> {
                signIn()
            }
            mButtonSingOut?.id -> {
                signOut()
            }
            mButtonDisconnect?.id -> {
                revokeAccess()
            }
            else -> {
                L.v("Identificador no Definido")
            }
        }
    }

    override fun initViews(){
        mProgressBar = findViewById(R.id.activity_login_progress_bar)
        mStatusTextView = findViewById(R.id.activity_login_status)
        mDetailTextView = findViewById(R.id.activity_login_detail)
        mContadorTextView = findViewById(R.id.activity_login_contador)
        mCambioInformacionTextView = findViewById(R.id.activity_login_informacion)
        mCambioInformacionButton = findViewById(R.id.activity_login_informacion_boton)
        mButtonSingIn = findViewById(R.id.activity_login_sign_in_button)
        mButtonSingOut = findViewById(R.id.activity_login_sign_out_button)
        mButtonDisconnect = findViewById(R.id.activity_login_disconnect_button)
        mDisconnectContainer = findViewById(R.id.activity_login_sign_out_and_disconnect)
        // Onclick listener
        mButtonSingIn?.setOnClickListener(this)
        mButtonSingOut?.setOnClickListener(this)
        mButtonDisconnect?.setOnClickListener(this)
    }
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initViews()
        initObservers()
        mUserViewModel.actualizarFicheroUsuario()
    }
    public override fun onStart(){
        super.onStart()
        val currentUser = mAuth?.currentUser
        updateUI(currentUser)
    }

    override fun onResume(){
        super.onResume()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        mAuth = FirebaseAuth.getInstance()
        updateUI(mAuth?.currentUser)
    }

    fun initViewModels(){
        mUserViewModel= ViewModelProviders.of(this).get(UserViewModel::class.java)
        mContadorTextView?.text = (++mUserViewModel.contador).toString()
        val nameObserver = Observer<String>{
            mCambioInformacionTextView?.text= it.toString()
        }
        mUserViewModel.getCurrentName().observe(this,nameObserver)
        mCambioInformacionButton?.setOnClickListener{
            mUserViewModel.getCurrentName().setValue("Hola Amigos !! Este es mi Contador ${mUserViewModel.contador} ")
        }
        lifecycle.addObserver(LocalizacionObserver(this))
    }
    fun initObservers(){

    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent){
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == mCodigoSingIn){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            }
            catch (e: ApiException){
                L.w("La Autenticacion con google ha Fallado", e)
                updateUI(null)
            }
        }

    }
    private fun updateUI(user: FirebaseUser?){
        mProgressBar?.visibility = View.GONE
        if(user != null){
            mStatusTextView?.text = "Google User: {user.email}"
            mDetailTextView?.text = "Firebase User: {user.uid}"
            mButtonSingIn?.visibility = View.GONE
            mButtonDisconnect?.visibility = View.VISIBLE
        }
        else{
            mStatusTextView?.setText("Signed Out")
            mDetailTextView?.text = null
            mButtonSingIn?.visibility = View.VISIBLE
            mButtonDisconnect?.visibility = View.GONE
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        L.d("firebaseAuthWithGoogle:" + acct.id!!)

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mAuth?.signInWithCredential(credential)
            ?.addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    L.d("signInWithCredential:success")
                    val user = mAuth?.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    L.e("signInWithCredential: ha habido un problema", task.exception)
                    Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show()
                    updateUI(null)
                }
                mProgressBar?.visibility = View.GONE
            }
    }

    private fun signIn(){
        startActivityForResult(mGoogleSignInClient?.signInIntent, mCodigoSingIn)
    }
    private fun signOut(){
        mAuth?.signOut()
    }

    private fun revokeAccess(){
        mAuth?.signOut()

        mGoogleSignInClient?.revokeAccess()?.addOnCompleteListener(this){
            updateUI(null)
        }
    }

}