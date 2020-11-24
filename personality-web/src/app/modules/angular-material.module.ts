import {NgModule} from '@angular/core';
import {
	MatAutocompleteModule,
	MatButtonModule,
	MatCardModule,
	MatCheckboxModule,
	MatChipsModule,
	MatDialogModule,
	MatDividerModule,
	MatIconModule,
	MatInputModule,
	MatListModule,
	MatMenuModule,
	MatPaginatorModule,
	MatProgressBarModule,
	MatProgressSpinnerModule,
	MatSelectModule,
	MatSliderModule,
	MatSnackBarModule,
	MatTabsModule,
	MatTableModule,
	MatToolbarModule,
	MatTooltipModule
} from '@angular/material';

@NgModule({
	exports: [MatAutocompleteModule, MatButtonModule, MatCardModule, MatChipsModule, MatDialogModule, MatTableModule, MatProgressSpinnerModule,
		MatToolbarModule, MatIconModule, MatMenuModule, MatDividerModule, MatPaginatorModule, MatProgressBarModule, MatSnackBarModule,
		MatTooltipModule, MatDialogModule, MatSelectModule, MatTabsModule, MatCheckboxModule, MatInputModule, MatListModule]
})
export class AngularMaterialModule {
}
