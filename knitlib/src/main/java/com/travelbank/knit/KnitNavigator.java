package com.travelbank.knit;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;

import java.lang.ref.WeakReference;

/**
 * Class that handles Navigation accross all views.
 * Instance is stored inside {@link Knit} and is often accessed inside
 * {@link KnitPresenter#getNavigator()}.
 *
 * @author Omer Ozer
 */

public class KnitNavigator {

    /**
     * Shared {@link Knit} instance.
     */
    private KnitInterface knitInterface;

    public KnitNavigator(KnitInterface knitInterface) {
        this.knitInterface = knitInterface;
    }

    /**
     * Stubbing initialization for when navigation to an activity is needed.
     *
     * @return Stubber class for Activity navigation {@link ActivityNavigator}.
     */
    public ActivityNavigator toActivity() {
        return new ActivityNavigator();
    }

    /**
     * Stubbing initialization for when navigation to a Fragment is needed.
     *
     * @return Stubber class for Fragment navigation {@link BaseFragmentNavigator}.
     */
    public BaseFragmentNavigator toFragment() {
        return new BaseFragmentNavigator();
    }

    /**
     * Base Navigor for all navigation stubbing.
     */
    public abstract class Navigator {
        public abstract Class<?> getTarget();
    }

    /**
     * Inner stubber class for Activity navigation.
     */
    public class ActivityNavigator extends Navigator {

        /**
         * Activity reference that is firing the navigation.
         */
        private WeakReference<Activity> from;

        /**
         * Fragment reference that is firing the navigation
         */
        private WeakReference<Fragment> fragment;

        /**
         * Support Fragment reference that is firing the navigation
         */
        private WeakReference<android.support.v4.app.Fragment> fragmentV4;

        /**
         * Activity to be started
         */
        private Class<? extends Activity> target;

        /**
         * Message instance to be delivered
         */
        private KnitMessage message;

        /**
         * Intent Flags
         */
        private int flags;


        /**
         * Intent directly provided
         */
        Intent intent;


        /**
         * Request code to be used if starting the activity for result;
         *
         * @see Activity#onActivityResult(int, int, Intent) ;
         */
        private int requestCode = Integer.MIN_VALUE;

        /**
         * Stubber/Setter for {@link this#from}
         *
         * @param viewObject view instance.
         *                   return stubbed instance
         */
        public ActivityNavigator fromParent(Object viewObject) {
            this.from = new WeakReference<Activity>(AndroidViewUtility.extractActivity(viewObject));
            return this;
        }

        public ActivityNavigator fromFragment(Object viewObject) {
            this.from = new WeakReference<Activity>(AndroidViewUtility.extractActivity(viewObject));
            this.fragment = new WeakReference<>((Fragment) viewObject);
            return this;
        }

        public ActivityNavigator fromSupportFragment(Object viewObject) {
            this.from = new WeakReference<Activity>(AndroidViewUtility.extractActivity(viewObject));
            this.fragmentV4 = new WeakReference<>((android.support.v4.app.Fragment) viewObject);
            return this;
        }

        /**
         * Stubber/Setter for {@link this#target}
         *
         * @param target target view class.
         *               return stubbed instance
         */
        public ActivityNavigator target(Class<? extends Activity> target) {
            this.target = target;
            return this;
        }

        /**
         * Stubber/Setter for {@link this#requestCode}
         *
         * @param requestCode request code for activity start intent
         * @returns stubbed instance
         */
        public ActivityNavigator forResult(int requestCode) {
            this.requestCode = requestCode;
            return this;
        }

        /**
         * Stubber/Setter for {@link this#intent}
         *
         * @param intent started intent directly provided to the navigator
         * @returns stubbed instance
         */
        public ActivityNavigator starterIntent(Intent intent){
            this.intent = intent;
            return this;
        }

        /**
         * Stubber/Setter for {@link this#message}
         *
         * @param message Message to be delivered.
         *                return stubbed instance
         */
        public ActivityNavigator setMessage(KnitMessage message) {
            this.message = message;
            return this;
        }

        /**
         * Setter for {@link this#flags}
         *
         * @param flags Intent flags for starting activity
         */
        public ActivityNavigator withFlags(int flags) {
            this.flags = flags;
            return this;
        }

        /**
         * Method that ends stubbing and starts the activity. Also sets the message on
         * {@link MessageTrain} for the target view.
         * If the {@link this#requestCode} is anything other than {@code Integer.MIN_VALUE}, than
         * it will call {@link Activity#startActivityForResult(Intent, int)}.
         */
        public void go() {
            if (message != null) {
                knitInterface.getMessageTrain().putMessageForView(
                        knitInterface.getViewToPresenterMap().getPresenterClassForView(target),
                        message);
            }


            if (from == null && intent ==null) {
                throw new RuntimeException("Knit: `From` has not been set. ");
            }

            if (target == null && intent ==null) {
                throw new RuntimeException("Knit: 'Target' has not been set");
            }

            Intent intent = this.intent == null ? new Intent(from.get(), target) : this.intent;
            intent.setFlags(flags);

            if (requestCode == Integer.MIN_VALUE) {
                if (fragmentV4 != null) {
                    fragmentV4.get().startActivity(intent);
                    return;
                }
                if (fragment != null) {
                    fragment.get().startActivity(intent);
                    return;
                }
                from.get().startActivity(intent);
                return;
            }
            if (fragmentV4 != null) {
                fragmentV4.get().startActivityForResult(intent, requestCode);
                return;
            }
            if (fragment != null) {
                fragment.get().startActivityForResult(intent, requestCode);
                return;
            }
            from.get().startActivityForResult(intent, requestCode);
        }

        /**
         * @return target view class.
         * @see Navigator
         */
        @Override
        public Class<?> getTarget() {
            return target;
        }
    }

    /**
     * Class that handles diverging in stubbing for regular and V4 fragment managers.
     */
    public class BaseFragmentNavigator {

        /**
         * Stub initializer for regular {@link FragmentManager}.
         *
         * @return instance of {@link FragmentNavigator}.
         */
        public FragmentNavigator withRegularFragmentManager() {
            return new FragmentNavigator();
        }

        /**
         * Stub initializer for regular {@link SupportFragmentNavigator}.
         *
         * @return instance of {@link SupportFragmentNavigator}.
         */
        public SupportFragmentNavigator withSupportFragmentManager() {
            return new SupportFragmentNavigator();
        }

    }

    /**
     * Main stubber class for regular fragment navigation.
     */
    public class FragmentNavigator extends Navigator {

        /**
         * Target fragment class. This is where the navigation will take the user.
         */
        private Class<? extends Fragment> target;

        /**
         * Regular fragment manager instance.
         */
        private WeakReference<FragmentManager> fragmentManager;

        /**
         * int Id for the view that contains the fragments.
         */
        private int containerId;

        /**
         * Message to be delivered to the target fragment upon initialization.
         */
        private KnitMessage message;


        /**
         * Stubber/Setter for {@link this#fragmentManager}
         *
         * @param viewObject View object that contains the fragment manager.
         *                   return stubbed instance
         */
        public FragmentNavigator from(Object viewObject) {
            this.fragmentManager = new WeakReference<>(
                    AndroidViewUtility.extractFragmentManager(viewObject));
            return this;
        }

        /**
         * Stubber/Setter for {@link this#containerId}
         *
         * @param containerId Id of the view that contains fragments.
         *                    return stubbed instance
         */
        public FragmentNavigator into(int containerId) {
            this.containerId = containerId;
            return this;
        }

        /**
         * Stubber/Setter for {@link this#target)}
         *
         * @param fragment Target fragment class that will be initialized and shown.
         *                 return stubbed instance
         */
        public FragmentNavigator target(Class<? extends Fragment> fragment) {
            this.target = fragment;
            return this;
        }

        /**
         * Stubber/Setter for {@link this#message}
         *
         * @param message Message to be delivered.
         *                return stubbed instance
         */
        public FragmentNavigator setMessage(KnitMessage message) {
            this.message = message;
            return this;
        }

        /**
         * Method that ends stubbing and starts the activity. Also sets the message on
         * {@link MessageTrain} for the target view.
         */
        public void go() {

            if (fragmentManager == null) {
                throw new RuntimeException("Knit: `From` has not been set. ");
            }

            if (target == null) {
                throw new RuntimeException("Knit: 'Target' has not been set");
            }

            Fragment fragment = AndroidViewUtility.initFragment(target);
            if (message != null) {
                knitInterface.getMessageTrain().putMessageForView(target, message);
            }
            fragmentManager.get()
                    .beginTransaction()
                    .replace(containerId, fragment)
                    .commit();
        }


        /**
         * @return target view class.
         * @see Navigator
         */
        @Override
        public Class<?> getTarget() {
            return target;
        }
    }

    public class SupportFragmentNavigator extends Navigator {

        /**
         * Target fragment class. This is where the navigation will take the user.
         */
        private Class<? extends android.support.v4.app.Fragment> targetV4;

        /**
         * Regular fragment manager instance.
         */
        private WeakReference<android.support.v4.app.FragmentManager> fragmentManager;

        /**
         * int Id for the view that contains the fragments.
         */
        private int containerId;


        /**
         * Message to be delivered to the target fragment upon initialization.
         */
        private KnitMessage message;

        /**
         * Stubber/Setter for {@link this#target)}
         *
         * @param fragment Target fragment class that will be initialized and shown.
         *                 return stubbed instance
         */
        public SupportFragmentNavigator target(
                Class<? extends android.support.v4.app.Fragment> fragment) {
            this.targetV4 = fragment;
            return this;
        }

        /**
         * Stubber/Setter for {@link this#fragmentManager}
         *
         * @param viewObject View object that contains the fragment manager.
         *                   return stubbed instance
         */
        public SupportFragmentNavigator from(Object viewObject) {
            this.fragmentManager = new WeakReference<>(
                    AndroidViewUtility.extractSupportFragmentManager(viewObject));
            return this;
        }

        /**
         * Stubber/Setter for {@link this#containerId}
         *
         * @param containerId Id of the view that contains fragments.
         *                    return stubbed instance
         */
        public SupportFragmentNavigator into(int containerId) {
            this.containerId = containerId;
            return this;
        }

        /**
         * Stubber/Setter for {@link this#message}
         *
         * @param message Message to be delivered.
         *                return stubbed instance
         */
        public SupportFragmentNavigator setMessage(KnitMessage message) {
            this.message = message;
            return this;
        }

        /**
         * Method that ends stubbing and starts the activity. Also sets the message on
         * {@link MessageTrain} for the target view.
         */
        public void go() {

            if (fragmentManager == null) {
                throw new RuntimeException("Knit: `From` has not been set. ");
            }

            if (targetV4 == null) {
                throw new RuntimeException("Knit: 'Target' has not been set");
            }

            android.support.v4.app.Fragment fragment = AndroidViewUtility.initSupportFragment(
                    targetV4);
            if (message != null) {
                knitInterface.getMessageTrain().putMessageForView(targetV4, message);
            }
            fragmentManager.get()
                    .beginTransaction()
                    .replace(containerId, fragment)
                    .commit();
        }

        /**
         * @return target view class.
         * @see Navigator
         */
        @Override
        public Class<?> getTarget() {
            return targetV4;
        }
    }


}
