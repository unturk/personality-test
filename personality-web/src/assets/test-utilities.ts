import {noop} from 'rxjs';
import {MatDialogRef} from '@angular/material';

class MockMatDialogRef {
	afterClosed = () => noop();
}

export const MockMatDialogRefProvider = {provide: MatDialogRef, useClass: MockMatDialogRef};
