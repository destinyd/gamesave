LOCAL_PATH:= $(call my-dir)
#################
include $(CLEAR_VARS)
LOCAL_MODULE_TAGS := optional
LOCAL_STATIC_JAVA_LIBRARIES := libRootTools
LOCAL_SRC_FILES := $(call all-java-files-under, src)
LOCAL_PACKAGE_NAME := GameSaveManager
LOCAL_CERTIFICATE := platform
include $(BUILD_PACKAGE)
# Use the folloing include to make our test apk.
# include $(call all-makefiles-under,$(LOCAL_PATH))
##############################################
include $(CLEAR_VARS)
LOCAL_PREBUILT_STATIC_JAVA_LIBRARIES := libRootTools:RootTools-1.7.jar
include $(BUILD_MULTI_PREBUILT)

# Use the folloing include to make our test apk.
#include $(call all-makefiles-under,$(LOCAL_PATH))
