package scripts;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;

public class FixController {
    public static void main(String[] args) throws Exception {
        String path = "e:/WebTMDT/src/controller/PostAdController.java";
        String content = new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
        
        String oldCode = "        // Táº¡o thÆ° má»¥c náº¿u chÆ°a cÃ³\n" +
                         "        File fileSaveDir = new File(uploadFilePath);\n" +
                         "        if (!fileSaveDir.exists()) {\n" +
                         "            fileSaveDir.mkdirs();\n" +
                         "        }\n" +
                         "\n" +
                         "        List<ProductImage> images = new ArrayList<>();\n" +
                         "        boolean isFirst = true;\n" +
                         "\n" +
                         "        Collection<Part> parts = request.getParts();\n" +
                         "        for (Part part : parts) {\n" +
                         "            if (part.getName().equals(\"images\") && part.getSize() > 0) {\n" +
                         "                // Ä áº·t tÃªn file ngáº«u nhiÃªn Ä‘á»ƒ trÃ¡nh trÃ¹ng láº·p\n" +
                         "                String fileName = UUID.randomUUID().toString() + \"_\" + extractFileName(part);\n" +
                         "                part.write(uploadFilePath + File.separator + fileName);\n" +
                         "\n" +
                         "                ProductImage img = new ProductImage();\n" +
                         "                img.setImageUrl(UPLOAD_DIR + \"/\" + fileName);\n" +
                         "                img.setPrimary(isFirst);\n" +
                         "                images.add(img);\n" +
                         "                \n" +
                         "                isFirst = false; // Bá»©c áº£nh Ä‘áº§u tiÃªn sáº½ Ä‘Æ°á»£c chá» n lÃ m áº£nh bÃ¬a\n" +
                         "            }\n" +
                         "        }";

        String newCode = "        // Thu muc luu vao ma nguon\n" +
                         "        String sourceFilePath = \"e:\" + File.separator + \"WebTMDT\" + File.separator + \"web\" + File.separator + UPLOAD_DIR.replace(\"/\", File.separator);\n" +
                         "\n" +
                         "        File fileSaveDir = new File(uploadFilePath);\n" +
                         "        if (!fileSaveDir.exists()) fileSaveDir.mkdirs();\n" +
                         "        \n" +
                         "        File sourceSaveDir = new File(sourceFilePath);\n" +
                         "        if (!sourceSaveDir.exists()) sourceSaveDir.mkdirs();\n" +
                         "\n" +
                         "        List<ProductImage> images = new ArrayList<>();\n" +
                         "        boolean isFirst = true;\n" +
                         "\n" +
                         "        Collection<Part> parts = request.getParts();\n" +
                         "        for (Part part : parts) {\n" +
                         "            if (part.getName().equals(\"images\") && part.getSize() > 0) {\n" +
                         "                String fileName = UUID.randomUUID().toString() + \"_\" + extractFileName(part);\n" +
                         "                \n" +
                         "                part.write(uploadFilePath + File.separator + fileName);\n" +
                         "                \n" +
                         "                try {\n" +
                         "                    Files.copy(\n" +
                         "                        Paths.get(uploadFilePath + File.separator + fileName),\n" +
                         "                        Paths.get(sourceFilePath + File.separator + fileName),\n" +
                         "                        StandardCopyOption.REPLACE_EXISTING\n" +
                         "                    );\n" +
                         "                } catch (Exception e) {}\n" +
                         "\n" +
                         "                ProductImage img = new ProductImage();\n" +
                         "                img.setImageUrl(UPLOAD_DIR + \"/\" + fileName);\n" +
                         "                img.setPrimary(isFirst);\n" +
                         "                images.add(img);\n" +
                         "                isFirst = false;\n" +
                         "            }\n" +
                         "        }";
                         
        // If exact match fails due to encoding, we can do substring replacement
        int startIndex = content.indexOf("        // T");
        int endIndex = content.indexOf("        // 4.");
        if (startIndex != -1 && endIndex != -1) {
            content = content.substring(0, startIndex) + newCode + "\n\n" + content.substring(endIndex);
            Files.write(Paths.get(path), content.getBytes(StandardCharsets.UTF_8));
            System.out.println("Replaced successfully via index!");
        } else {
            System.out.println("Could not find indices.");
        }
    }
}