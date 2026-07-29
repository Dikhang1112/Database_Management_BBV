package execution_engine.strategy;

import execution_engine.domain.Tuple;

public class BitmapScanStrategy implements ScanStrategy {

    @Override
    public Tuple scan() {
        System.out.println("  -> [ScanStrategy: Bitmap Scan] Scanning via Bitmap Index.");
        return null;
    }
}
