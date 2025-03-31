package ProductManangementMVC.Model;

public class Validate {

        // First Regex is ChoosePaginationAndMenuRegex
        public final String CPMRegex = "^(N|P|F|L|G|W|R|U|D|S|SA|SE|UN|BA|RE|E|UU|UI)$";
        public final String PRODUCT_NAME_REGEX = "^[A-Za-z][A-Za-z0-9\\s\\W]*$";
        public final String PRODUCT_UNIT_PRICE = "^\\d+(\\.\\d{1,2})?$";
        public final String PRODUCT_STOCK_QTY = "^[1-9]\\d*$";
        public final String INT = "^\\d+$";
        public final String UI_UU = "^(ui|uu|b)$";
        public final String SI_SU = "^(si|su|b)$";
        public final String ARE_YOU_SURE = "^(YES|NO|Y|N)$";
        public final String FILE= "^[a-zA-Z0-9_-]+$";


}
