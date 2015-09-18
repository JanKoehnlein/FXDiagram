package de.fxdiagram.idea.classdiagram;

import com.intellij.psi.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by koehnlein on 28/05/15.
 */
public class PsiJavaUtil {

    public static List<PsiField> getAttributes(PsiClass psiClass) {
        return Arrays.stream(psiClass.getFields()).filter(PsiJavaUtil::isAttribute).collect(Collectors.toList());
    }

    public static List<PsiField> getReferences(PsiClass psiClass) {
        return Arrays.stream(psiClass.getFields()).filter(
                field -> !isAttribute(field)
        ).collect(Collectors.toList());
    }

    public static List<PsiMethod> getOperations(PsiClass psiClass) {
        return Arrays.stream(psiClass.getMethods()).filter(
                method -> !method.isConstructor()
        ).collect(Collectors.toList());
    }

    public static List<PsiClassType> getSuperTypes(PsiClass psiClass) {
        return Stream.concat(Arrays.stream(psiClass.getExtendsList().getReferencedTypes()),
                Arrays.stream(psiClass.getImplementsList().getReferencedTypes())
        ).collect(Collectors.toList());
    }

    private static boolean isAttribute(PsiField psiField) {
        PsiType type = psiField.getType();
        return type instanceof PsiPrimitiveType
                || (type instanceof PsiClassType
                && ((PsiClassType) type).resolve() != null
                && "java.lang.String".equals(((PsiClassType) type).resolve().getQualifiedName()));

    }
}
