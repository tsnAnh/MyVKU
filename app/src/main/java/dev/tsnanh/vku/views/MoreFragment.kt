/* * Copyright (c) 2020 My VKU by tsnAnh */package dev.tsnanh.vku.viewsimport android.app.ActivityOptionsimport android.content.Intentimport android.os.Bundleimport android.view.LayoutInflaterimport android.view.Viewimport android.view.ViewGroupimport androidx.databinding.DataBindingUtilimport androidx.fragment.app.Fragmentimport androidx.fragment.app.viewModelsimport androidx.lifecycle.lifecycleScopeimport androidx.navigation.fragment.FragmentNavigatorExtrasimport androidx.navigation.fragment.findNavControllerimport com.google.android.gms.auth.api.signin.GoogleSignInimport com.google.android.gms.auth.api.signin.GoogleSignInClientimport com.google.android.gms.auth.api.signin.GoogleSignInOptionsimport com.google.android.material.dialog.MaterialAlertDialogBuilderimport com.google.android.material.transition.MaterialFadeThroughimport dagger.hilt.android.AndroidEntryPointimport dev.tsnanh.vku.Rimport dev.tsnanh.vku.activities.SettingsActivityimport dev.tsnanh.vku.activities.WelcomeActivityimport dev.tsnanh.vku.databinding.FragmentMoreBindingimport dev.tsnanh.vku.viewmodels.MoreViewModelimport kotlinx.coroutines.Dispatchersimport kotlinx.coroutines.ExperimentalCoroutinesApiimport kotlinx.coroutines.tasks.asDeferredimport kotlinx.coroutines.withContextimport java.lang.Exceptionimport javax.inject.Inject@Deprecated(message = "no longer and will be completely remove in alpha version")@AndroidEntryPointclass MoreFragment : Fragment() {    private lateinit var binding: FragmentMoreBinding    private val viewModel: MoreViewModel by viewModels()    @Inject    lateinit var mGoogleSignInClient: GoogleSignInClient    override fun onCreate(savedInstanceState: Bundle?) {        super.onCreate(savedInstanceState)        enterTransition = MaterialFadeThrough()        exitTransition = MaterialFadeThrough()    }    override fun onCreateView(        inflater: LayoutInflater,        container: ViewGroup?,        savedInstanceState: Bundle?,    ): View? {        binding = DataBindingUtil            .inflate(inflater, R.layout.fragment_more, container, false)        return binding.root    }    @ExperimentalCoroutinesApi    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {        super.onViewCreated(view, savedInstanceState)        binding.viewModel = viewModel        lifecycleScope.launchWhenCreated {            try {                val accountDeferred = mGoogleSignInClient.silentSignIn().asDeferred()                val account = withContext(Dispatchers.Default) {                    accountDeferred.await()                }                binding.user = account            } catch (e: Exception) {                // Ignore            }        }        binding.apply {            attendance.isEnabled = false            attendance.isClickable = false        }        viewModel.navigateToSettings.observe(viewLifecycleOwner) {            it?.let {                /* findNavController().navigate(                    MoreFragmentDirections.activitySettings(),                    FragmentNavigatorExtras(binding.buttonSettings to "settings")                ) */                val intent = Intent(requireContext(), SettingsActivity::class.java)                val options = ActivityOptions.makeSceneTransitionAnimation(                    requireActivity(),                    binding.buttonSettings,                    "settings"                )                startActivity(intent, options.toBundle())                viewModel.onNavigatedToSettings()            }        }        viewModel.navigateToTeacherEvaluation.observe(viewLifecycleOwner) {            it?.let {                val extras = FragmentNavigatorExtras(                    binding.buttonTeacherEvaluation to "teacherEvaluation"                )                findNavController().navigate(                    MoreFragmentDirections.actionNavigationMoreToNavigationTeacherEvaluation(),                    extras                )                viewModel.onNavigatedToTeacherEvaluation()            }        }        viewModel.signOut.observe(viewLifecycleOwner) {            it?.let {                val builder = MaterialAlertDialogBuilder(requireContext())                    .setTitle(requireContext().getString(R.string.text_sign_out))                    .setMessage(getString(R.string.text_are_u_sure))                    .setPositiveButton(requireContext().getString(R.string.text_ok)) { dialog, _ ->                        mGoogleSignInClient.signOut().addOnCompleteListener { task ->                            if (task.isComplete) {                                dialog.dismiss()                                startActivity(Intent(requireContext(), WelcomeActivity::class.java))                                requireActivity().finish()                            }                        }                    }                    .setNegativeButton(requireContext().getString(R.string.text_cancel)) { dialog, _ ->                        dialog.dismiss()                    }                builder.create().show()                viewModel.onSignedOut()            }        }        viewModel.navigateToElearning.observe(viewLifecycleOwner) {            it?.let {                val launchIntent =                    requireContext().packageManager.getLaunchIntentForPackage("vn.udn.vku.elearning")                launchIntent?.let { intent ->                    startActivity(intent)                }                viewModel.onNavigatedToElearning()            }        }    }}