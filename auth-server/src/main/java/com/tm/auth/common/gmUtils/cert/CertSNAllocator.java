package com.tm.auth.common.gmUtils.cert;

import java.math.BigInteger;

public interface CertSNAllocator {
    BigInteger nextSerialNumber() throws Exception;
}
