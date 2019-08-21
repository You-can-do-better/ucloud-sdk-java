package com.xiaolong.ucloud;

import com.xiaolong.ucloud.ufile.UFile;
import com.xiaolong.ucloud.ufile.UFileClientBuilder;
import com.xiaolong.ucloud.ufile.model.UObject;
import com.xiaolong.ucloud.ufile.model.UObjectInputStream;
import com.xiaolong.ucloud.ufile.request.UResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;

/**
 * 删除文件测试
 *
 * @author york
 */
public class UObjectOperationTest
{
    static final String LOCAL_TEST_FILE = "C:\\Users\\lanxiaolong\\Desktop\\elk.txt";
    private UFile ufile;
    static final String TEST_BUCKET = "grp01";
    static final String TEST_KEY = "elk.txt";
    String publicKey = "";
    String privateKey = "";

    @Before
    public void setup()
    {
        ufile = UFileClientBuilder.defaultClient(publicKey, privateKey);
    }

    @After
    public void teardown()
    {
        try {
            ufile.shutdown();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test(/*expected = Test.None.class*/)
    public void test1()
            throws Exception
    {
        /*if (ufile.doesObjectExist(TEST_BUCKET, TEST_KEY)) {
            ufile.deleteObject(TEST_BUCKET, TEST_KEY);
        }*/
        File testFile = new File(LOCAL_TEST_FILE);
        //UResponse newObjectMetadata = ufile.putObject(TEST_BUCKET, TEST_KEY, testFile, "image/png");
        UResponse response = ufile.putObject(TEST_BUCKET, testFile.getName(), testFile, "text/plain");
        System.out.println(response.getRetCode());

        /*UObjectMetadata existObjectMetadatae = ufile.getObjectMetadata(TEST_BUCKET, TEST_KEY);
        assertEquals(existObjectMetadatae.getETag(), newObjectMetadata.getETag());
        assertEquals(testFile.length(), existObjectMetadatae.getContentLength());
        assertEquals("jpeg", existObjectMetadatae.getContentType());*/


        /*existObjectMetadatae = ufile.getObject(TEST_BUCKET, TEST_KEY, new File("/Users/kimzhang/Downloads/temp.pdf"));
        String deleteObjectKey = ufile.deleteObject(TEST_BUCKET, TEST_KEY);
        assertEquals(deleteObjectKey, TEST_KEY);*/
    }

    @Test(/*expected = Test.None.class*/)
    public void test2()
            throws Exception
    {
        if (ufile.doesObjectExist(TEST_BUCKET, TEST_KEY)) {
            ufile.deleteObject(TEST_BUCKET, TEST_KEY);
        }
        /*for (int i = 0; i < 16; i++) {
            String testKey = String.format("test2/object-%02d", i);
            File testFile = new File(LOCAL_TEST_FILE);
            ufile.putObject(TEST_BUCKET, testKey, testFile);
        }

        UObjectListing objectListing = ufile.listObjects(TEST_BUCKET, "test2", 10);
        assertEquals(objectListing.getObjectSummaries().size(), 10);
        for (int i = 0; i < 10; i++) {
            String testKey = String.format("test2/object-%02d", i);
            assertEquals(objectListing.getObjectSummaries().get(i).getObjectKey(), testKey);
        }

        UObjectListing nextObjectListing = ufile.listNextBatchOfObjects(objectListing);
        assertEquals(nextObjectListing.getObjectSummaries().size(), 6);
        for (int i = 0; i < 6; i++) {
            String testKey = String.format("test2/object-%02d", 10 + i);
            assertEquals(nextObjectListing.getObjectSummaries().get(i).getObjectKey(), testKey);
        }

        nextObjectListing = ufile.listNextBatchOfObjects(nextObjectListing);
        assertEquals(nextObjectListing, null);

        for (int i = 0; i < 16; i++) {
            String testKey = String.format("test2/object-%02d", i);
            String deleteObjectKey = ufile.deleteObject(TEST_BUCKET, testKey);
            assertEquals(deleteObjectKey, testKey);
        }*/
    }

    /**
     * 测试文件偏移读写UFile Object
     */
    @Test(expected = Test.None.class)
    public void test3()
            throws Exception
    {
        UObject object = ufile.getObject("tpc", "tpch-s1/part.tbl", 32, 32);
        UObjectInputStream stream = object.getObjectContent();
        assertEquals("tpc", object.getBucketName());
        assertEquals("tpch-s1/part.tbl", object.getObjectKey());
        int length = stream.available();
        assertEquals(32, length);
        byte[] buffer = new byte[32];
        stream.read(buffer);
        assertEquals("olate lace|Manufacturer#1|Brand#", new String(buffer));
    }

    /**
     * 测试文件上传是指定ContentType
     */
    @Test(expected = Test.None.class)
    public void test4()
            throws Exception
    {
        if (ufile.doesObjectExist(TEST_BUCKET, TEST_KEY)) {
            ufile.deleteObject(TEST_BUCKET, TEST_KEY);
        }
        File testFile = new File(LOCAL_TEST_FILE);
        UResponse newObjectMetadata = ufile.putObject(TEST_BUCKET, TEST_KEY, testFile, "image/png");

        UResponse existObjectMetadatae = ufile.getObjectMetadata(TEST_BUCKET, TEST_KEY);
        /*assertEquals(existObjectMetadatae.getETag(), newObjectMetadata.getETag());
        assertEquals(testFile.length(), existObjectMetadatae.getContentLength());
        assertEquals("image/png", existObjectMetadatae.getContentType());*/

//        existObjectMetadatae = ufile.getObject(TEST_BUCKET, TEST_KEY, new File("/Users/kimzhang/Downloads/temp.pdf"));
//        String deleteObjectKey = ufile.deleteObject(TEST_BUCKET, TEST_KEY);
//        assertEquals(deleteObjectKey, TEST_KEY);
    }

//
//    @Test
//    public void test2()
//            throws ExecutionException, InterruptedException
//    {
//        String bucketName = "";
//        String configPath = "";
//        //加载配置项
//
//        // specify your concurrency
//        int concurrency = 3;
//        // total count in each thread
//        int count = 3;
//
//        ExecutorService taskExecutor =
//                Executors.newFixedThreadPool(concurrency);
//
//        ConcurrentHashMap<Integer, Long> result = new ConcurrentHashMap<Integer, Long>();
//
//        AtomicInteger threadId = new AtomicInteger(0);
//
//        LinkedList<Future> futures = new LinkedList<Future>();
//        for (int i = 0; i < concurrency; ++i) {
//            String key = String.format("m-test %d", i);
//            String filePath = String.format("/Users/york/java-sdk-test/m-test-%d", i);
//            PutFileRunner putFileRunner = new PutFileRunner(
//                    threadId, key, bucketName, filePath, count, result);
//            futures.add(taskExecutor.submit(putFileRunner));
//        }
//
//        for (Future future : futures) {
//            future.get();
//        }
//
//        for (Map.Entry<Integer, Long> entry : result.entrySet()) {
//            System.out.println("thread id: " + entry.getKey() + ", average latency: " + entry.getValue());
//        }
//
//        taskExecutor.shutdown();
//        while (!taskExecutor.isTerminated()) {
//            try {
//                taskExecutor.awaitTermination(10, TimeUnit.SECONDS);
//            }
//            catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private static class PutFileRunner
//            implements Runnable
//    {
//        private String key;
//        private String bucket;
//        private String filePath;
//        private int count;
//        private ConcurrentHashMap result;
//        private int id;
//
//        public PutFileRunner(AtomicInteger threadId, String key,
//                String bucket, String filePath,
//                int count, ConcurrentHashMap<Integer, Long> result)
//        {
//            this.key = key;
//            this.bucket = bucket;
//            this.filePath = filePath;
//            this.count = count;
//            this.result = result;
//            this.id = threadId.getAndIncrement();
//        }
//
//        @Override
//        public void run()
//        {
//            long elapsedTime = 0;
//
//            for (int i = 0; i < count; i++) {
//                UFileCredentials credentials = new UFileCredentials();
//                credentials.loadConfig(UFILE_CONFIG_FILE);
//                UFile ufileClient = UFileClientBuilder.standard(credentials);
//                UFileRequest request = new UFileRequest();
//                request.setBucketName(bucket);
//                request.setKey(key);
//                request.setFilePath(filePath);
//
//                PutSender sender = new PutSender();
//                sender.makeAuth(ufileClient, request);
//
//                long begin = System.currentTimeMillis();
//                UFileResponse response = sender.send(ufileClient, request);
//                long end = System.currentTimeMillis();
//
//                if (response != null) {
//                    System.out.println("finish " + i + "th running");
//                    elapsedTime += (end - begin);
//                }
//
//                //consume the http response body
//                InputStream inputStream = response.getContent();
//                if (inputStream != null) {
//                    try {
//                        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
//                        String s = "";
//                        while ((s = reader.readLine()) != null) {
//                            System.out.println(s);
//                        }
//                    }
//                    catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    finally {
//                        if (inputStream != null) {
//                            try {
//                                inputStream.close();
//                            }
//                            catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                }
//
//                ufileClient.shutdown();
//            }
//
//            result.put(id, elapsedTime / count);
//        }
//    }
//
//    public static final int CONCURRENT_COUNT = 3;
//
//    @Test
//    public void test3()
//    {
//        String bucketName = "";
//        String key = "";
//        String filePath = "";
//        String configPath = "";
//
//        UFileCredentials credentials = new UFileCredentials();
//        credentials.loadConfig(UFILE_CONFIG_FILE);
//
//        UFileRequest request = new UFileRequest();
//        request.setBucketName(bucketName);
//        request.setKey(key);
//        request.setFilePath(filePath);
//
//        System.out.println("Multi-Upload Test BEGIN ...");
//        multiUpload(request);
//        System.out.println("Multi-Upload Test END ...\n\n");
//    }
//
//    private int calPartCount(String filePath, int partSize)
//    {
//        File file = new File(filePath);
//        int partCount = (int) (file.length() / partSize);
//        if (file.length() % partSize != 0) {
//            partCount++;
//        }
//        return partCount;
//    }
//
//    private void multiUpload(UFileRequest request)
//    {
//        UFileCredentials credentials = new UFileCredentials();
//        credentials.loadConfig(UFILE_CONFIG_FILE);
//
//        UFile ufileClient = null;
//        // 初始化分片请求
//        InitMultiBody initMultiBody = null;
//        try {
//            ufileClient = UFileClientBuilder.standard(credentials);
//            initMultiBody = initiateMultiUpload(ufileClient, request);
//        }
//        finally {
//            ufileClient.shutdown();
//        }
//
//        if (null == initMultiBody) {
//            System.out.println("failed to initiate the multipart Upload");
//            return;
//        }
//
//        // 上传各个分片
//        int partSize = initMultiBody.getBlkSize();
//        String uploadId = initMultiBody.getUploadId();
//        int partCount = calPartCount(request.getFilePath(), partSize);
//
//        ExecutorService pool = Executors.newFixedThreadPool(CONCURRENT_COUNT);
//
//        SortedMap<Integer, String> eTags = Collections
//                .synchronizedSortedMap(new TreeMap<Integer, String>());
//
//        File file = new File(request.getFilePath());
//        for (long i = 0; i < partCount; i++) {
//            long start = partSize * i;
//            long curPartSize = partSize < file.length() - start ? partSize
//                    : file.length() - start;
//
//            pool.execute(new UploadPartRunnable(request, uploadId, (int) i, partSize
//                    * i, curPartSize, eTags));
//        }
//
//        pool.shutdown();
//        while (!pool.isTerminated()) {
//            try {
//                pool.awaitTermination(10, TimeUnit.SECONDS);
//            }
//            catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//
//        if (eTags.size() != partCount) {
//            throw new IllegalStateException("One or more parts failed");
//        }
//
//        // 完成分片上传
//        String newKey = "newly" + request.getKey();
//        ufileClient = null;
//        try {
//            ufileClient = UFileClientBuilder.standard(credentials);
//            finishMultiUpload(ufileClient, request, uploadId, eTags, newKey);
//        }
//        finally {
//            ufileClient.shutdown();
//        }
//    }
//
//    private void finishMultiUpload(UFile ufileClient,
//            UFileRequest request, String uploadId,
//            SortedMap<Integer, String> eTags, String newKey)
//    {
//        FinishMultiSender sender = new FinishMultiSender(uploadId, eTags,
//                newKey);
//        sender.makeAuth(ufileClient, request);
//        UFileResponse finishRes = sender.send(ufileClient, request);
//        if (finishRes != null) {
//            if (finishRes.getStatusLine().getStatusCode() != 200) {
//                if (finishRes.getContent() != null) {
//                    ufileClient.closeErrorResponse(finishRes);
//                }
//            }
//            else {
//                InputStream inputStream = finishRes.getContent();
//                Reader reader = new InputStreamReader(inputStream);
//                Gson gson = new Gson();
//                FinishMultiBody body = gson.fromJson(reader,
//                        FinishMultiBody.class);
//                String bodyJson = gson.toJson(body);
//                System.out.println(bodyJson);
//                if (inputStream != null) {
//                    try {
//                        inputStream.close();
//                    }
//                    catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
//    }
//
//    private static InitMultiBody initiateMultiUpload(UFile ufileClient,
//            UFileRequest request)
//    {
//        InitiateMultiSender init = new InitiateMultiSender();
//        init.makeAuth(ufileClient, request);
//        UFileResponse initRes = init.send(ufileClient, request);
//        if (initRes != null) {
//            System.out.println("status line: " + initRes.getStatusLine());
//            Header[] headers = initRes.getHeaders();
//            for (int i = 0; i < headers.length; i++) {
//                System.out.println("header " + headers[i].getName() + " : "
//                        + headers[i].getValue());
//            }
//
//            if (initRes.getStatusLine().getStatusCode() != 200) {
//                if (initRes.getContent() != null) {
//                    ufileClient.closeErrorResponse(initRes);
//                }
//                return null;
//            }
//            else {
//                InputStream inputStream = initRes.getContent();
//                Reader reader = new InputStreamReader(inputStream);
//                Gson gson = new Gson();
//                InitMultiBody body = gson.fromJson(reader, InitMultiBody.class);
//                String bodyJson = gson.toJson(body);
//                System.out.println(bodyJson);
//                if (inputStream != null) {
//                    try {
//                        inputStream.close();
//                    }
//                    catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//                return body;
//            }
//        }
//        return null;
//    }
//
//    private class UploadPartRunnable
//            implements Runnable
//    {
//        private UFileRequest request;
//        private String uploadId;
//        private int partNumber;
//        private long start;
//        private long size;
//        private SortedMap<Integer, String> eTags;
//
//        UploadPartRunnable(UFileRequest request, String uploadId,
//                int partNumber, long start, long partSize,
//                SortedMap<Integer, String> eTags)
//        {
//            try {
//                this.request = (UFileRequest) request.clone();
//            }
//            catch (CloneNotSupportedException e) {
//                e.printStackTrace();
//            }
//            this.uploadId = uploadId;
//            this.partNumber = partNumber;
//            this.start = start;
//            this.size = partSize;
//            this.eTags = eTags;
//        }
//
//        @Override
//        public void run()
//        {
//            UFileCredentials credentials = new UFileCredentials();
//            credentials.loadConfig(UFILE_CONFIG_FILE);
//            UFile ufileClient = null;
//            try {
//                ufileClient = UFileClientBuilder.standard(credentials);
//                String etag;
//                etag = uploadPart(ufileClient, uploadId, partNumber, start,
//                        size);
//                if (etag != null) {
//                    eTags.put(partNumber, etag);
//                }
//            }
//            finally {
//                ufileClient.shutdown();
//            }
//        }
//
//        private String uploadPart(UFile ufileClient, String uploadId,
//                int partNumber, long start, long size)
//        {
//            UploadPartSender sender = new UploadPartSender(uploadId,
//                    partNumber, start, size);
//            sender.makeAuth(ufileClient, request);
//            UFileResponse partRes = sender.send(ufileClient, request);
//
//            // consume the http response body
//            if (partRes != null) {
//                if (partRes.getStatusLine().getStatusCode() != 200) {
//                    if (partRes.getContent() != null) {
//                        ufileClient.closeErrorResponse(partRes);
//                    }
//                    return null;
//                }
//                else {
//                    InputStream inputStream = partRes.getContent();
//                    Reader reader = new InputStreamReader(inputStream);
//                    Gson gson = new Gson();
//                    PartBody body = gson.fromJson(reader, PartBody.class);
//                    String bodyJson = gson.toJson(body);
//                    System.out.println(bodyJson);
//                    if (inputStream != null) {
//                        try {
//                            inputStream.close();
//                        }
//                        catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }
//
//            // now we only need the head item 'etag'
//
//            Header[] headers = partRes.getHeaders();
//            for (int i = 0; i < headers.length; i++) {
//                if ("ETag".equalsIgnoreCase(headers[i].getName())) {
//                    return headers[i].getValue();
//                }
//            }
//            return null;
//        }
//    }
//
//    @Test
//    public void test4()
//    {
//        String bucketName = "";
//        String saveAsPath = "";
//
//        //加载配置项
//        UFileCredentials credentials = new UFileCredentials();
//        credentials.loadConfig(UFILE_CONFIG_FILE);
//
//        UFileRequest request = new UFileRequest();
//        request.setBucketName(bucketName);
//        request.setKey(TEST_KEY);
//
//        // url链接的有效期为一天，从当前unix时间戳开始，86400秒后结束
//
//        System.out.println("DownloadUrl Test BEGIN ...");
//
//        boolean isPrivate = true;
//        UFile ufileClient = UFileClientBuilder.standard(credentials);
//        int ttl = 86400;
//        /*
//         * 针对私有的Bucket，为了防止盗链，建议生成的链接的有效期为ttl > 0; 如果 ttl == 0, 那么生成的链接永久有效
//         */
//        String url = downloadUrl(ufileClient, request, ttl, isPrivate);
//        System.out.println(url);
//
//        downloadFileFromUrl(url, saveAsPath);
//
//        System.out.println("DownloadUrl Test END ...\n\n");
//    }
//
//    private void downloadFileFromUrl(String urlStr, String saveAsPath)
//    {
//        URL url;
//        InputStream inputStream = null;
//        OutputStream outputStream = null;
//        try {
//            url = new URL(urlStr);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            inputStream = conn.getInputStream();
//            outputStream = new BufferedOutputStream(new FileOutputStream(
//                    saveAsPath));
//            int bufSize = 1024 * 4;
//            byte[] buffer = new byte[bufSize];
//            int bytesRead;
//            while ((bytesRead = inputStream.read(buffer)) > 0) {
//                outputStream.write(buffer, 0, bytesRead);
//            }
//        }
//        catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }
//        finally {
//            if (inputStream != null) {
//                try {
//                    inputStream.close();
//                }
//                catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            if (outputStream != null) {
//                try {
//                    outputStream.close();
//                }
//                catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//    private String downloadUrl(UFile ufileClient,
//            UFileRequest request, int ttl, boolean isPrivate)
//    {
//        DownloadUrl downloadUrl = new DownloadUrl();
//        return downloadUrl.getUrl(ufileClient, request, ttl, isPrivate);
//    }
}
