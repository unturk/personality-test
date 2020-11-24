import {async, ComponentFixture, TestBed} from '@angular/core/testing';
import {PersonalityTestDialogComponent} from "./personality-test-dialog.component";
import {NO_ERRORS_SCHEMA} from "@angular/core";
import {NoopAnimationsModule} from "@angular/platform-browser/animations";
import {AngularMaterialModule} from "../../modules/angular-material.module";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {MockMatDialogRefProvider} from "../../../assets/test-utilities";

describe('PersonalityTestDialogComponent', () => {
	let component: PersonalityTestDialogComponent;
	let fixture: ComponentFixture<PersonalityTestDialogComponent>;

	beforeEach(async(() => {
		TestBed.configureTestingModule({
			declarations: [PersonalityTestDialogComponent],
			schemas: [NO_ERRORS_SCHEMA],
			imports: [NoopAnimationsModule, AngularMaterialModule, HttpClientTestingModule],
			providers: [MockMatDialogRefProvider]
		})
			.compileComponents();
	}));

	beforeEach(() => {
		fixture = TestBed.createComponent(PersonalityTestDialogComponent);
		component = fixture.componentInstance;
		fixture.detectChanges();

	});

	it('should create', () => {
		expect(component).toBeTruthy();
	});
});
