package tech.openedgn.net.server.web.consts

import java.io.File

/**
 * Content-Type 属性
 *
 */

enum class ContentType(val fileType: String, val application: String) {
    TYPE_907(".907", "drawing/907"),
    TYPE_AIFF(".aif", "audio/aiff"),
    TYPE_AIFF_1(".aiff", "audio/aiff"),
    TYPE_AIFF_2(".aifc", "audio/aiff"),
    TYPE_ASA(".asa", "text/asa"),
    TYPE_ASP(".asp", "text/asp"),
    TYPE_AVI(".avi", "video/avi"),
    TYPE_BASIC(".au", "audio/basic"),
    TYPE_BASIC_1(".snd", "audio/basic"),
    TYPE_CSS(".css", "text/css"),
    TYPE_FAX(".fax", "image/fax"),
    TYPE_FRACTALS(".fif", "application/fractals"),
    TYPE_FUTURESPLASH(".spl", "application/futuresplash"),
    TYPE_GIF(".gif", "image/gif"),
    TYPE_H323(".323", "text/h323"),
    TYPE_HTA(".hta", "application/hta"),
    TYPE_HTML(".html", "text/html"),
    TYPE_HTML_1(".htx", "text/html"),
    TYPE_HTML_2(".jsp", "text/html"),
    TYPE_HTML_3(".plg", "text/html"),
    TYPE_HTML_4(".xhtml", "text/html"),
    TYPE_HTML_5(".htm", "text/html"),
    TYPE_HTML_6(".stm", "text/html"),
    TYPE_IULS(".uls", "text/iuls"),
    TYPE_JAVA(".java", "java/*"),
    TYPE_JAVA_1(".class", "java/*"),
    TYPE_JPEG(".jpe", "image/jpeg"),
    TYPE_JPEG_1(".jpeg", "image/jpeg"),
    TYPE_JPEG_2(".jfif", "image/jpeg"),
    TYPE_JPEG_3(".jpg", "image/jpeg"),
    TYPE_MAC_BINHEX40(".hqx", "application/mac-binhex40"),
    TYPE_MID(".mid", "audio/mid"),
    TYPE_MID_1(".rmi", "audio/mid"),
    TYPE_MID_2(".midi", "audio/mid"),
    TYPE_MP1(".mp1", "audio/mp1"),
    TYPE_MP2(".mp2", "audio/mp2"),
    TYPE_MP3(".mp3", "audio/mp3"),
    TYPE_MPEG(".mp2v", "video/mpeg"),
    TYPE_MPEG4(".m4e", "video/mpeg4"),
    TYPE_MPEG4_1(".mp4", "video/mpeg4"),
    TYPE_MPEGURL(".m3u", "audio/mpegurl"),
    TYPE_MPEG_1(".mpv2", "video/mpeg"),
    TYPE_MPG(".mpeg", "video/mpg"),
    TYPE_MPG_1(".mpv", "video/mpg"),
    TYPE_MPG_2(".mpg", "video/mpg"),
    TYPE_MSACCESS(".mdb", "application/msaccess"),
    TYPE_MSWORD(".doc", "application/msword"),
    TYPE_MSWORD_1(".rtf", "application/msword"),
    TYPE_MSWORD_2(".wiz", "application/msword"),
    TYPE_MSWORD_3(".dot", "application/msword"),
    TYPE_OCTET_STREAM("*", "application/octet-stream"),
    TYPE_PDF(".pdf", "application/pdf"),
    TYPE_PDF_1(".pdf", "application/pdf"),
    TYPE_PICS_RULES(".prf", "application/pics-rules"),
    TYPE_PKCS10(".p10", "application/pkcs10"),
    TYPE_PKCS7_MIME(".p7c", "application/pkcs7-mime"),
    TYPE_PKCS7_MIME_1(".p7m", "application/pkcs7-mime"),
    TYPE_PKCS7_SIGNATURE(".p7s", "application/pkcs7-signature"),
    TYPE_PKIX_CRL(".crl", "application/pkix-crl"),
    TYPE_PLAIN(".sor", "text/plain"),
    TYPE_PLAIN_1(".sol", "text/plain"),
    TYPE_PLAIN_2(".txt", "text/plain"),
    TYPE_PNETVUE(".net", "image/pnetvue"),
    TYPE_PNG(".png", "image/png"),
    TYPE_POSTSCRIPT(".ps", "application/postscript"),
    TYPE_POSTSCRIPT_1(".ai", "application/postscript"),
    TYPE_POSTSCRIPT_2(".eps", "application/postscript"),
    TYPE_RAT_FILE(".rat", "application/rat-file"),
    TYPE_RFC822(".mhtml", "message/rfc822"),
    TYPE_RFC822_1(".nws", "message/rfc822"),
    TYPE_RFC822_2(".eml", "message/rfc822"),
    TYPE_RFC822_3(".mht", "message/rfc822"),
    TYPE_RN_MPEG(".mpga", "audio/rn-mpeg"),
    TYPE_SCPLS(".pls", "audio/scpls"),
    TYPE_SCPLS_1(".xpl", "audio/scpls"),
    TYPE_SCRIPTLET(".wsc", "text/scriptlet"),
    TYPE_SDP(".sdp", "application/sdp"),
    TYPE_SMIL(".smil", "application/smil"),
    TYPE_SMIL_1(".smi", "application/smil"),
    TYPE_STREAMINGMEDIA(".ssm", "application/streamingmedia"),
    TYPE_TIFF(".tif", "image/tiff"),
    TYPE_TIFF_1(".tiff", "image/tiff"),
    TYPE_TIFF_2(".tif", "image/tiff"),
    TYPE_VND_ADOBE_EDN(".edn", "application/vnd.adobe.edn"),
    TYPE_VND_ADOBE_PDX(".pdx", "application/vnd.adobe.pdx"),
    TYPE_VND_ADOBE_RMF(".rmf", "application/vnd.adobe.rmf"),
    TYPE_VND_ADOBE_WORKFLOW(".awf", "application/vnd.adobe.workflow"),
    TYPE_VND_ADOBE_XDP(".xdp", "application/vnd.adobe.xdp"),
    TYPE_VND_ADOBE_XFD(".xfd", "application/vnd.adobe.xfd"),
    TYPE_VND_ADOBE_XFDF(".xfdf", "application/vnd.adobe.xfdf"),
    TYPE_VND_ANDROID_PACKAGE_ARCHIVE(".apk", "application/vnd.android.package-archive"),
    TYPE_VND_DWF(".dwf", "Model/vnd.dwf"),
    TYPE_VND_FDF(".fdf", "application/vnd.fdf"),
    TYPE_VND_IPHONE(".ipa", "application/vnd.iphone"),
    TYPE_VND_MS_EXCEL(".xls", "application/vnd.ms-excel"),
    TYPE_VND_MS_PKI_CERTSTORE(".sst", "application/vnd.ms-pki.certstore"),
    TYPE_VND_MS_PKI_PKO(".pko", "application/vnd.ms-pki.pko"),
    TYPE_VND_MS_PKI_SECCAT(".cat", "application/vnd.ms-pki.seccat"),
    TYPE_VND_MS_PKI_STL(".stl", "application/vnd.ms-pki.stl"),
    TYPE_VND_MS_POWERPOINT(".ppa", "application/vnd.ms-powerpoint"),
    TYPE_VND_MS_POWERPOINT_1(".pps", "application/vnd.ms-powerpoint"),
    TYPE_VND_MS_POWERPOINT_2(".pwz", "application/vnd.ms-powerpoint"),
    TYPE_VND_MS_POWERPOINT_3(".pot", "application/vnd.ms-powerpoint"),
    TYPE_VND_MS_POWERPOINT_4(".ppt", "application/vnd.ms-powerpoint"),
    TYPE_VND_MS_PROJECT(".mpd", "application/vnd.ms-project"),
    TYPE_VND_MS_PROJECT_1(".mpw", "application/vnd.ms-project"),
    TYPE_VND_MS_PROJECT_2(".mpp", "application/vnd.ms-project"),
    TYPE_VND_MS_PROJECT_3(".mpt", "application/vnd.ms-project"),
    TYPE_VND_MS_PROJECT_4(".mpx", "application/vnd.ms-project"),
    TYPE_VND_MS_WPL(".wpl", "application/vnd.ms-wpl"),
    TYPE_VND_RN_REALAUDIO(".ra", "audio/vnd.rn-realaudio"),
    TYPE_VND_RN_REALMEDIA(".rm", "application/vnd.rn-realmedia"),
    TYPE_VND_RN_REALMEDIA_SECURE(".rms", "application/vnd.rn-realmedia-secure"),
    TYPE_VND_RN_REALMEDIA_VBR(".rmvb", "application/vnd.rn-realmedia-vbr"),
    TYPE_VND_RN_REALPIX(".rp", "image/vnd.rn-realpix"),
    TYPE_VND_RN_REALPLAYER(".rnx", "application/vnd.rn-realplayer"),
    TYPE_VND_RN_REALSYSTEM_RJS(".rjs", "application/vnd.rn-realsystem-rjs"),
    TYPE_VND_RN_REALSYSTEM_RJT(".rjt", "application/vnd.rn-realsystem-rjt"),
    TYPE_VND_RN_REALSYSTEM_RMJ(".rmj", "application/vnd.rn-realsystem-rmj"),
    TYPE_VND_RN_REALSYSTEM_RMX(".rmx", "application/vnd.rn-realsystem-rmx"),
    TYPE_VND_RN_REALTEXT(".rt", "text/vnd.rn-realtext"),
    TYPE_VND_RN_REALTEXT3D(".r3t", "text/vnd.rn-realtext3d"),
    TYPE_VND_RN_REALVIDEO(".rv", "video/vnd.rn-realvideo"),
    TYPE_VND_RN_RECORDING(".rec", "application/vnd.rn-recording"),
    TYPE_VND_RN_RN_MUSIC_PACKAGE(".rmp", "application/vnd.rn-rn_music_package"),
    TYPE_VND_RN_RSML(".rsml", "application/vnd.rn-rsml"),
    TYPE_VND_SYMBIAN_INSTALL(".sis", "application/vnd.symbian.install"),
    TYPE_VND_SYMBIAN_INSTALL_1(".sisx", "application/vnd.symbian.install"),
    TYPE_VND_VISIO(".vdx", "application/vnd.visio"),
    TYPE_VND_VISIO_1(".vst", "application/vnd.visio"),
    TYPE_VND_VISIO_2(".vsw", "application/vnd.visio"),
    TYPE_VND_VISIO_3(".vtx", "application/vnd.visio"),
    TYPE_VND_VISIO_4(".vsd", "application/vnd.visio"),
    TYPE_VND_VISIO_5(".vss", "application/vnd.visio"),
    TYPE_VND_VISIO_6(".vsx", "application/vnd.visio"),
    TYPE_VND_WAP_WBMP(".wbmp", "image/vnd.wap.wbmp"),
    TYPE_VND_WAP_WML(".wml", "text/vnd.wap.wml"),
    TYPE_WAV(".wav", "audio/wav"),
    TYPE_WEBVIEWHTML(".htt", "text/webviewhtml"),
    TYPE_XML(".cml", "text/xml"),
    TYPE_XML_1(".dcd", "text/xml"),
    TYPE_XML_10(".xsl", "text/xml"),
    TYPE_XML_11(".biz", "text/xml"),
    TYPE_XML_12(".dtd", "text/xml"),
    TYPE_XML_13(".fo", "text/xml"),
    TYPE_XML_14(".math", "text/xml"),
    TYPE_XML_15(".mml", "text/xml"),
    TYPE_XML_16(".spp", "text/xml"),
    TYPE_XML_17(".svg", "text/xml"),
    TYPE_XML_18(".tld", "text/xml"),
    TYPE_XML_19(".vml", "text/xml"),
    TYPE_XML_2(".ent", "text/xml"),
    TYPE_XML_20(".vxml", "text/xml"),
    TYPE_XML_21(".xdr", "text/xml"),
    TYPE_XML_22(".xql", "text/xml"),
    TYPE_XML_23(".xsd", "text/xml"),
    TYPE_XML_24(".xslt", "text/xml"),
    TYPE_XML_3(".mtx", "text/xml"),
    TYPE_XML_4(".rdf", "text/xml"),
    TYPE_XML_5(".tsd", "text/xml"),
    TYPE_XML_6(".wsdl", "text/xml"),
    TYPE_XML_7(".xml", "text/xml"),
    TYPE_XML_8(".xq", "text/xml"),
    TYPE_XML_9(".xquery", "text/xml"),
    TYPE_X_(".", "application/x-"),
    TYPE_X_001(".001", "application/x-001"),
    TYPE_X_301(".301", "application/x-301"),
    TYPE_X_906(".906", "application/x-906"),
    TYPE_X_A11(".a11", "application/x-a11"),
    TYPE_X_ANV(".anv", "application/x-anv"),
    TYPE_X_BITTORRENT(".torrent", "tapplication/x-bittorrent"),
    TYPE_X_BMP(".bmp", "application/x-bmp"),
    TYPE_X_BOT(".bot", "application/x-bot"),
    TYPE_X_C4T(".c4t", "application/x-c4t"),
    TYPE_X_C90(".c90", "application/x-c90"),
    TYPE_X_CALS(".cal", "application/x-cals"),
    TYPE_X_CDR(".cdr", "application/x-cdr"),
    TYPE_X_CEL(".cel", "application/x-cel"),
    TYPE_X_CGM(".cgm", "application/x-cgm"),
    TYPE_X_CIT(".cit", "application/x-cit"),
    TYPE_X_CMP(".cmp", "application/x-cmp"),
    TYPE_X_CMX(".cmx", "application/x-cmx"),
    TYPE_X_COMPONENT(".htc", "text/x-component"),
    TYPE_X_COT(".cot", "application/x-cot"),
    TYPE_X_CSI(".csi", "application/x-csi"),
    TYPE_X_CUT(".cut", "application/x-cut"),
    TYPE_X_DBF(".dbf", "application/x-dbf"),
    TYPE_X_DBM(".dbm", "application/x-dbm"),
    TYPE_X_DBX(".dbx", "application/x-dbx"),
    TYPE_X_DCX(".dcx", "application/x-dcx"),
    TYPE_X_DGN(".dgn", "application/x-dgn"),
    TYPE_X_DIB(".dib", "application/x-dib"),
    TYPE_X_DRW(".drw", "application/x-drw"),
    TYPE_X_DWF(".dwf", "application/x-dwf"),
    TYPE_X_DWG(".dwg", "application/x-dwg"),
    TYPE_X_DXB(".dxb", "application/x-dxb"),
    TYPE_X_DXF(".dxf", "application/x-dxf"),
    TYPE_X_EBX(".etd", "application/x-ebx"),
    TYPE_X_EMF(".emf", "application/x-emf"),
    TYPE_X_EPI(".epi", "application/x-epi"),
    TYPE_X_FRM(".frm", "application/x-frm"),
    TYPE_X_G4(".cg4", "application/x-g4"),
    TYPE_X_G4_1(".g4", "application/x-g4"),
    TYPE_X_G4_2(".ig4", "application/x-g4"),
    TYPE_X_GBR(".gbr", "application/x-gbr"),
    TYPE_X_GL2(".gl2", "application/x-gl2"),
    TYPE_X_GP4(".gp4", "application/x-gp4"),
    TYPE_X_HGL(".hgl", "application/x-hgl"),
    TYPE_X_HMR(".hmr", "application/x-hmr"),
    TYPE_X_HPGL(".hpg", "application/x-hpgl"),
    TYPE_X_HPL(".hpl", "application/x-hpl"),
    TYPE_X_HRF(".hrf", "application/x-hrf"),
    TYPE_X_ICB(".icb", "application/x-icb"),
    TYPE_X_ICO(".ico", "application/x-ico"),
    TYPE_X_ICON(".ico", "image/x-icon"),
    TYPE_X_ICQ(".uin", "application/x-icq"),
    TYPE_X_IFF(".iff", "application/x-iff"),
    TYPE_X_IGS(".igs", "application/x-igs"),
    TYPE_X_IMG(".img", "application/x-img"),
    TYPE_X_INTERNET_SIGNUP(".isp", "application/x-internet-signup"),
    TYPE_X_INTERNET_SIGNUP_1(".ins", "application/x-internet-signup"),
    TYPE_X_IPHONE(".iii", "application/x-iphone"),
    TYPE_X_IVF(".IVF", "video/x-ivf"),
    TYPE_X_JAVASCRIPT(".mocha", "application/x-javascript"),
    TYPE_X_JAVASCRIPT_1(".js", "application/x-javascript"),
    TYPE_X_JAVASCRIPT_2(".ls", "application/x-javascript"),
    TYPE_X_JPE(".jpe", "application/x-jpe"),
    TYPE_X_JPG(".jpg", "application/x-jpg"),
    TYPE_X_LAPLAYER_REG(".lar", "application/x-laplayer-reg"),
    TYPE_X_LATEX(".latex", "application/x-latex"),
    TYPE_X_LA_LMS(".lmsff", "audio/x-la-lms"),
    TYPE_X_LBM(".lbm", "application/x-lbm"),
    TYPE_X_LIQUID_FILE(".la1", "audio/x-liquid-file"),
    TYPE_X_LIQUID_SECURE(".lavs", "audio/x-liquid-secure"),
    TYPE_X_LTR(".ltr", "application/x-ltr"),
    TYPE_X_MAC(".mac", "application/x-mac"),
    TYPE_X_MDB(".mdb", "application/x-mdb"),
    TYPE_X_MEI_AAC(".acp", "audio/x-mei-aac"),
    TYPE_X_MI(".mi", "application/x-mi"),
    TYPE_X_MIL(".mil", "application/x-mil"),
    TYPE_X_MMXP(".mxp", "application/x-mmxp"),
    TYPE_X_MPEG(".m2v", "video/x-mpeg"),
    TYPE_X_MPEG_1(".mps", "video/x-mpeg"),
    TYPE_X_MPEG_2(".m1v", "video/x-mpeg"),
    TYPE_X_MPEG_3(".mpe", "video/x-mpeg"),
    TYPE_X_MPG(".mpa", "video/x-mpg"),
    TYPE_X_MSDOWNLOAD(".dll", "application/x-msdownload"),
    TYPE_X_MSDOWNLOAD_1(".exe", "application/x-msdownload"),
    TYPE_X_MS_ASF(".asf", "video/x-ms-asf"),
    TYPE_X_MS_ASF_1(".asx", "video/x-ms-asf"),
    TYPE_X_MS_ODC(".odc", "text/x-ms-odc"),
    TYPE_X_MS_WAX(".wax", "audio/x-ms-wax"),
    TYPE_X_MS_WM(".wm", "video/x-ms-wm"),
    TYPE_X_MS_WMA(".wma", "audio/x-ms-wma"),
    TYPE_X_MS_WMD(".wmd", "application/x-ms-wmd"),
    TYPE_X_MS_WMV(".wmv", "video/x-ms-wmv"),
    TYPE_X_MS_WMX(".wmx", "video/x-ms-wmx"),
    TYPE_X_MS_WMZ(".wmz", "application/x-ms-wmz"),
    TYPE_X_MS_WVX(".wvx", "video/x-ms-wvx"),
    TYPE_X_MUSICNET_DOWNLOAD(".mnd", "audio/x-musicnet-download"),
    TYPE_X_MUSICNET_STREAM(".mns", "audio/x-musicnet-stream"),
    TYPE_X_NETCDF(".cdf", "application/x-netcdf"),
    TYPE_X_NRF(".nrf", "application/x-nrf"),
    TYPE_X_OUT(".out", "application/x-out"),
    TYPE_X_PC5(".pc5", "application/x-pc5"),
    TYPE_X_PCI(".pci", "application/x-pci"),
    TYPE_X_PCL(".pcl", "application/x-pcl"),
    TYPE_X_PCX(".pcx", "application/x-pcx"),
    TYPE_X_PERL(".pl", "application/x-perl"),
    TYPE_X_PGL(".pgl", "application/x-pgl"),
    TYPE_X_PIC(".pic", "application/x-pic"),
    TYPE_X_PKCS12(".p12", "application/x-pkcs12"),
    TYPE_X_PKCS12_1(".pfx", "application/x-pkcs12"),
    TYPE_X_PKCS7_CERTIFICATES(".p7b", "application/x-pkcs7-certificates"),
    TYPE_X_PKCS7_CERTIFICATES_1(".spc", "application/x-pkcs7-certificates"),
    TYPE_X_PKCS7_CERTREQRESP(".p7r", "application/x-pkcs7-certreqresp"),
    TYPE_X_PLT(".plt", "application/x-plt"),
    TYPE_X_PNG(".png", "application/x-png"),
    TYPE_X_PN_REALAUDIO(".rmm", "audio/x-pn-realaudio"),
    TYPE_X_PN_REALAUDIO_1(".ram", "audio/x-pn-realaudio"),
    TYPE_X_PN_REALAUDIO_PLUGIN(".rpm", "audio/x-pn-realaudio-plugin"),
    TYPE_X_PPM(".ppm", "application/x-ppm"),
    TYPE_X_PPT(".ppt", "application/x-ppt"),
    TYPE_X_PR(".pr", "application/x-pr"),
    TYPE_X_PRN(".prn", "application/x-prn"),
    TYPE_X_PRT(".prt", "application/x-prt"),
    TYPE_X_PS(".eps", "application/x-ps"),
    TYPE_X_PS_1(".ps", "application/x-ps"),
    TYPE_X_PTN(".ptn", "application/x-ptn"),
    TYPE_X_RAS(".ras", "application/x-ras"),
    TYPE_X_RED(".red", "application/x-red"),
    TYPE_X_RGB(".rgb", "application/x-rgb"),
    TYPE_X_RLC(".rlc", "application/x-rlc"),
    TYPE_X_RLE(".rle", "application/x-rle"),
    TYPE_X_RTF(".rtf", "application/x-rtf"),
    TYPE_X_SAM(".sam", "application/x-sam"),
    TYPE_X_SAT(".sat", "application/x-sat"),
    TYPE_X_SDW(".sdw", "application/x-sdw"),
    TYPE_X_SGI_MOVIE(".movie", "video/x-sgi-movie"),
    TYPE_X_SHOCKWAVE_FLASH(".mfp", "application/x-shockwave-flash"),
    TYPE_X_SHOCKWAVE_FLASH_1(".swf", "application/x-shockwave-flash"),
    TYPE_X_SILVERLIGHT_APP(".xap", "application/x-silverlight-app"),
    TYPE_X_SLB(".slb", "application/x-slb"),
    TYPE_X_SLD(".sld", "application/x-sld"),
    TYPE_X_SLK(".slk", "drawing/x-slk"),
    TYPE_X_SMK(".smk", "application/x-smk"),
    TYPE_X_STUFFIT(".sit", "application/x-stuffit"),
    TYPE_X_STY(".sty", "application/x-sty"),
    TYPE_X_TDF(".tdf", "application/x-tdf"),
    TYPE_X_TG4(".tg4", "application/x-tg4"),
    TYPE_X_TGA(".tga", "application/x-tga"),
    TYPE_X_TIF(".tif", "application/x-tif"),
    TYPE_X_TOP(".top", "drawing/x-top"),
    TYPE_X_TROFF_MAN(".man", "application/x-troff-man"),
    TYPE_X_VCARD(".vcf", "text/x-vcard"),
    TYPE_X_VDA(".vda", "application/x-vda"),
    TYPE_X_VPEG005(".vpg", "application/x-vpeg005"),
    TYPE_X_VSD(".vsd", "application/x-vsd"),
    TYPE_X_VST(".vst", "application/x-vst"),
    TYPE_X_WB1(".wb1", "application/x-wb1"),
    TYPE_X_WB2(".wb2", "application/x-wb2"),
    TYPE_X_WB3(".wb3", "application/x-wb3"),
    TYPE_X_WK3(".wk3", "application/x-wk3"),
    TYPE_X_WK4(".wk4", "application/x-wk4"),
    TYPE_X_WKQ(".wkq", "application/x-wkq"),
    TYPE_X_WKS(".wks", "application/x-wks"),
    TYPE_X_WMF(".wmf", "application/x-wmf"),
    TYPE_X_WP6(".wp6", "application/x-wp6"),
    TYPE_X_WPD(".wpd", "application/x-wpd"),
    TYPE_X_WPG(".wpg", "application/x-wpg"),
    TYPE_X_WQ1(".wq1", "application/x-wq1"),
    TYPE_X_WR1(".wr1", "application/x-wr1"),
    TYPE_X_WRI(".wri", "application/x-wri"),
    TYPE_X_WRK(".wrk", "application/x-wrk"),
    TYPE_X_WS(".ws2", "application/x-ws"),
    TYPE_X_WS_1(".ws", "application/x-ws"),
    TYPE_X_X509_CA_CERT(".der", "application/x-x509-ca-cert"),
    TYPE_X_X509_CA_CERT_1(".cer", "application/x-x509-ca-cert"),
    TYPE_X_X509_CA_CERT_2(".crt", "application/x-x509-ca-cert"),
    TYPE_X_XLS(".xls", "application/x-xls"),
    TYPE_X_XLW(".xlw", "application/x-xlw"),
    TYPE_X_XWD(".xwd", "application/x-xwd"),
    TYPE_X_X_B(".x_b", "application/x-x_b"),
    TYPE_X_X_T(".x_t", "application/x-x_t");


    companion object {

        /**
         * 得到文件 `Content-Type` 类型
         *
         * 注意，此方案是通过文件名来判断的，并不准确
         *
         * @param file File 文件位置
         * @return ContentType 文件的类型
         */
        fun getFileContentType(file: File) = getFileNameContentType(file.name)

        /**
         *  通过文件名称得到文件 Content-Type 类型
         *
         *  此方案仅依赖简单的后缀名判断
         *
         * @param fileName String 文件名称
         * @return ContentType 得到的类型
         */
        fun getFileNameContentType(fileName: String): ContentType {
            for (contentType in values().iterator()) {
                if (fileName.endsWith(contentType.fileType)) {
                    return contentType
                }
            }
            return TYPE_OCTET_STREAM
        }

        /**
         * 通过Content-Type得到文件 Content-Type 类型
         *
         *
         *  @param contentType String `Content-Type` 字符串
         * @return ContentType Content-Type 类型
         */
        fun getContentType(contentType: String): ContentType {
            val c = contentType.toLowerCase()
            for (t in values().iterator()) {
                if (c.contains(t.application)) {
                    return t
                }
            }
            return TYPE_OCTET_STREAM
        }
    }
}

fun File.getContentType() = ContentType.getFileContentType(this)
