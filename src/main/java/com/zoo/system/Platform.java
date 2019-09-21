package com.zoo.system;

import com.sun.jna.Native;

/**
 * 平台工具类
 *
 */
public final class Platform {
	private Platform(){}
	
	/**
	 * 斜杠
	 */
	public static final String SLASH = "/";
	
	
	/**
	 * 反斜杠
	 */
	public static final String BACKSLASH = "\\";
	
	
	/**
	 * windows动态链接库的后缀
	 */
	public static final String DLL = ".dll";
	
	/**
	 * linux共享库的后缀
	 */
	public static final String SO = ".so";
	
	
	private static boolean IS_WINDOWS = false;
	private static boolean IS_WINDOWSCE = false;
	private static boolean IS_MAC = false;
	private static boolean IS_LINUX = false;
	private static boolean IS_ANDROID = false;
	private static boolean IS_SOLARIS = false;
	private static boolean IS_UNIX = false;
	private static boolean IS_AIX = false;
	private static boolean IS_FREEBSD = false;
	private static boolean IS_OPENBSD = false;
	private static boolean IS_NETBSD = false;
	private static boolean IS_GNU = false;
	private static boolean IS_KFREEBSD = false;
	
	private static final boolean IS64;
	private static final boolean IS_INTEL;
	private static final boolean IS_PPC;
	private static final boolean IS_ARM;
	private static final boolean IS_SPARC;
	
	
	/**
	 * 获取不同级别的文件目录之间的连接符,若是windows平台则返回'\',其他平台返回'/'
	 * @return
	 */
	public static String slash(){
		return isWindows() ? BACKSLASH : SLASH;
	}
	
	/**
	 * 获取当前系统平台下使用的jni文件的后缀<br/>
	 * windows:.dll<br/>
	 * other:.so
	 * @return
	 */
	public static String jniSuffix() {
		return isWindows() ? DLL : SO;
	}
	

    /** Whether read-only (final) fields within Structures are supported. */
    public static final boolean RO_FIELDS;
    /** Whether this platform provides NIO Buffers. */
    public static final boolean HAS_BUFFERS;
    /** Whether this platform provides the AWT Component class; also false if
     * running headless.
     */
    public static final boolean HAS_AWT;
    /** Canonical name of this platform's math library. */
    public static final String MATH_LIBRARY_NAME;
    /** Canonical name of this platform's C runtime library. */
    public static final String C_LIBRARY_NAME;
    /** Whether in-DLL callbacks are supported. */
    public static final boolean HAS_DLL_CALLBACKS;

    /** Current platform architecture. */
    public static final String ARCH;

    static {
        String osName = System.getProperty("os.name");
        String osNameLower = osName.toLowerCase();
        if (osName.startsWith("Linux")) {
            if ("dalvik".equals(System.getProperty("java.vm.name").toLowerCase())) {
            	IS_ANDROID = true;
            }
            else {
            	IS_LINUX = true;
            }
        }
        else if (osName.startsWith("AIX")) {
        	IS_AIX = true;
        	IS_UNIX = true;
        }
        else if (osName.startsWith("Mac") || osName.startsWith("Darwin")) {
        	IS_MAC = true;
        }
        else if (osName.startsWith("Windows CE")) {
        	IS_WINDOWSCE = true;
        }
        else if (osName.startsWith("Windows")) {
        	IS_WINDOWS = true;
        }
        else if (osName.startsWith("Solaris") || osName.startsWith("SunOS")) {
        	IS_SOLARIS = true;
        }
        else if (osName.startsWith("FreeBSD")) {
        	IS_FREEBSD = true;
        }
        else if (osName.startsWith("OpenBSD")) {
        	IS_OPENBSD = true;
        }
        else if (osName.equalsIgnoreCase("gnu")) {
        	IS_GNU = true;
        }
        else if (osName.equalsIgnoreCase("gnu/kfreebsd")) {
        	IS_KFREEBSD = true;
        }
        else if (osName.equalsIgnoreCase("netbsd")) {
        	IS_NETBSD = true;
        }
        else if(osNameLower.contains("nix") || osNameLower.contains("nux")) {
        	IS_UNIX = true;
        }
        boolean hasBuffers = false;
        try {
            Class.forName("java.nio.Buffer");
            hasBuffers = true;
        }
        catch(ClassNotFoundException e) {
        }
        // NOTE: we used to do Class.forName("java.awt.Component"), but that
        // has the unintended side effect of actually loading AWT native libs,
        // which can be problematic
        HAS_AWT = !IS_WINDOWSCE && !IS_ANDROID && !IS_AIX;
        HAS_BUFFERS = hasBuffers;
        RO_FIELDS = !IS_WINDOWSCE;
        C_LIBRARY_NAME = IS_WINDOWS ? "msvcrt" : IS_WINDOWSCE ? "coredll" : "c";
        MATH_LIBRARY_NAME = IS_WINDOWS ? "msvcrt" : IS_WINDOWSCE ? "coredll" : "m";
        HAS_DLL_CALLBACKS = IS_WINDOWS;
        ARCH = System.getProperty("os.arch").toLowerCase().trim();
        
        String model = System.getProperty("sun.arch.data.model", System.getProperty("com.ibm.vm.bitmode"));
		if (model != null) {
			IS64 = "64".equals(model);
		}else if ("x86_64".equals(ARCH)
				|| "ia64".equals(ARCH)
				|| "ppc64".equals(ARCH)
				|| "sparcv9".equals(ARCH)
				|| "amd64".equals(ARCH)) {
			IS64 = true;
		}else {
			IS64 = Native.POINTER_SIZE == 8;
		}
		
		IS_INTEL = ARCH.equals("i386")
	            || ARCH.startsWith("i686")
	            || ARCH.equals("x86")
	            || ARCH.equals("x86_64")
	            || ARCH.equals("amd64");
		
		IS_PPC = ARCH.equals("ppc")
	            || ARCH.equals("ppc64")
	            || ARCH.equals("powerpc")
	            || ARCH.equals("powerpc64");
		
		IS_ARM = ARCH.startsWith("arm");
		
		IS_SPARC = ARCH.startsWith("sparc");
		
    }
    
    
    /**
	 * 判断当前系统是否是mac平台
	 * @return
	 */
    public static final boolean isMac() {
        return IS_MAC;
    }
    
    
    /**
	 * 判断当前系统是否是android平台
	 * @return
	 */
    public static final boolean isAndroid() {
        return IS_ANDROID;
    }
    
    
    /**
	 * 判断当前系统是否是linux平台
	 * @return
	 */
    public static final boolean isLinux() {
        return IS_LINUX;
    }
    
    
    /**
	 * 判断当前系统是否是unix平台
	 * @return
	 */
    public static final boolean isUnix() {
        return IS_UNIX;
    }
    public static final boolean isAIX() {
        return IS_AIX;
    }
    public static final boolean isWindowsCE() {
        return IS_WINDOWSCE;
    }


    /**
	 * 判断当前系统是否是windows平台
	 * @return
	 */
    public static final boolean isWindows() {
        return IS_WINDOWS || IS_WINDOWSCE;
    }
    
    
    /**
	 * 判断当前系统是否是solaris平台
	 * @return
	 */
    public static final boolean isSolaris() {
        return IS_SOLARIS;
    }
    public static final boolean isFreeBSD() {
        return IS_FREEBSD;
    }
    public static final boolean isOpenBSD() {
        return IS_OPENBSD;
    }
    public static final boolean isNetBSD() {
        return IS_NETBSD;
    }
    public static final boolean isGNU() {
        return IS_GNU;
    }
    public static final boolean iskFreeBSD() {
        return IS_KFREEBSD;
    }
    public static final boolean isX11() {
        //check filesystem for /usr/X11 or some other X11-specific test
        return !Platform.isWindows() && !Platform.isMac();
    }
    public static final boolean hasRuntimeExec() {
        if (isWindowsCE() && "J9".equals(System.getProperty("java.vm.name")))
            return false;
        return true;
    }
    
    /**
	 * 判断当前系统平台是否是64位
	 * @return
	 */
    public static final boolean is64Bit() {
    	return IS64;
    }

    public static final boolean isIntel() {
        return IS_INTEL;
    }

    public static final boolean isPPC() {
        return IS_PPC;
    }

    public static final boolean isARM() {
        return IS_ARM;
    }

    public static final boolean isSPARC() {
        return IS_SPARC;
    }

}
